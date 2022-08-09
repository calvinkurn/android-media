package com.tokopedia.play.channel.analytic

import android.content.Context
import androidx.lifecycle.Lifecycle
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.analytic.PlayAnalytic2
import com.tokopedia.play.analytic.PlayNewAnalytic
import com.tokopedia.play.analytic.ProductAnalyticHelper
import com.tokopedia.play.channel.ui.component.KebabIconUiComponent
import com.tokopedia.play.channel.ui.component.ProductCarouselUiComponent
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.event.AtcSuccessEvent
import com.tokopedia.play.view.uimodel.event.BuySuccessEvent
import com.tokopedia.play.view.uimodel.event.PlayViewerNewUiEvent
import com.tokopedia.play.view.uimodel.state.PlayViewerNewUiState
import com.tokopedia.play_common.eventbus.EventBus
import com.tokopedia.trackingoptimizer.TrackingQueue
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Created by kenny.hadisaputra on 06/07/22
 */
class PlayChannelAnalyticManager @AssistedInject constructor(
    private val analytic: PlayAnalytic,
    private val newAnalytic: PlayNewAnalytic,
    private val analytic2Factory: PlayAnalytic2.Factory,
    private val dispatchers: CoroutineDispatchers,
    private val trackingQueue: TrackingQueue,
    @Assisted private val productAnalyticHelper: ProductAnalyticHelper,
) {

    @AssistedFactory
    interface Factory {
        fun create(
            productAnalyticHelper: ProductAnalyticHelper,
        ) : PlayChannelAnalyticManager
    }

    private var analytic2 : PlayAnalytic2? = null

    fun observe(
        scope: CoroutineScope,
        viewEvent: EventBus<Any>,
        uiState: StateFlow<PlayViewerNewUiState>,
        uiEvent: Flow<PlayViewerNewUiEvent>,
        lifecycle: Lifecycle,
    ) {
        scope.launch(dispatchers.computation) {
            uiState.collectLatest {
                if (analytic2 != null || it.channel.channelInfo.id.isBlank()) return@collectLatest

                analytic2 = analytic2Factory.create(
                    trackingQueue = trackingQueue,
                    channelInfo = it.channel.channelInfo,
                )
            }
        }

        scope.launch(dispatchers.computation) {
            viewEvent.subscribe().collect {
                when (it) {
                    is ProductCarouselUiComponent.Event.OnClicked -> {
                        onCarouselProductClicked(it.product, it.position)
                    }
                    is ProductCarouselUiComponent.Event.OnImpressed -> {
                        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED) ||
                                it.productMap.isEmpty()) return@collect

                        it.productMap.forEach { entry ->
                            if (!entry.key.isPinned) return@forEach
                            analytic2?.impressPinnedProductInCarousel(entry.key, entry.value)
                        }
                        productAnalyticHelper.trackImpressedProducts(it.productMap)
                    }
                    KebabIconUiComponent.Event.OnClicked -> analytic.clickKebabMenu()
                }
            }
        }

        scope.launch {
            uiEvent.collect {
                when (it) {
                    is BuySuccessEvent -> {
                        if (!it.product.isPinned || !it.isProductFeatured) return@collect
                        analytic2?.buyPinnedProductInCarousel(
                            product = it.product,
                            cartId = it.cartId,
                            quantity = it.product.minQty,
                        )
                    }
                    is AtcSuccessEvent -> {
                        if (!it.product.isPinned || !it.isProductFeatured) return@collect

                        analytic2?.atcPinnedProductInCarousel(
                            product = it.product,
                            cartId = it.cartId,
                            quantity = it.product.minQty,
                        )

                        /**
                         * Because Toaster doesn't have any identifier and show listener,
                         * this is currently the best way to do this
                         */
                        analytic2?.impressToasterAtcPinnedProductCarousel()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun onCarouselProductClicked(product: PlayProductUiModel.Product, position: Int) {
        if (product.isPinned) analytic2?.clickPinnedProductInCarousel(product, position)

        if (product.isTokoNow) {
            newAnalytic.clickFeaturedProductNow(product, position)
        } else {
            analytic.clickFeaturedProduct(product, position)
        }
    }

    fun sendPendingTrackers(partnerType: PartnerType) {
        productAnalyticHelper.sendImpressedFeaturedProducts(partnerType)
    }
}
