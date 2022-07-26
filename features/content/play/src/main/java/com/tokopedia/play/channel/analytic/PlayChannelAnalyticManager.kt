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
    @Assisted context: Context,
    @Assisted private val productAnalyticHelper: ProductAnalyticHelper,
) {

    @AssistedFactory
    interface Factory {
        fun create(
            context: Context,
            productAnalyticHelper: ProductAnalyticHelper,
        ) : PlayChannelAnalyticManager
    }

    private val trackingQueue = TrackingQueue(context)
    private var analytic2 : PlayAnalytic2? = null

    fun observe(
        scope: CoroutineScope,
        event: EventBus<Any>,
        uiState: Flow<PlayViewerNewUiState>,
        lifecycle: Lifecycle,
    ) {
        scope.launch(dispatchers.computation) {
            uiState.collectLatest {
                if (analytic2 != null) return@collectLatest
                analytic2 = analytic2Factory.create(
                    trackingQueue = trackingQueue,
                    channelInfo = it.channel.channelInfo,
                )
            }
        }

        scope.launch(dispatchers.computation) {
            event.subscribe().collect {
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
    }

    private fun onCarouselProductClicked(product: PlayProductUiModel.Product, position: Int) {
        if (product.isPinned) analytic2?.clickPinnedProductInCarousel(product, position)

        if (product.isTokoNow) {
            newAnalytic.clickFeaturedProduct(product, position)
        } else {
            analytic.clickFeaturedProduct(product, position)
        }
    }

    fun sendPendingTrackers(partnerType: PartnerType) {
        productAnalyticHelper.sendImpressedFeaturedProducts(partnerType)
    }
}
