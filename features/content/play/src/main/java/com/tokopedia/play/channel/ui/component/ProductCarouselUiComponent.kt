package com.tokopedia.play.channel.ui.component

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.databinding.ViewProductFeaturedBinding
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.ui.component.UiComponent
import com.tokopedia.play.ui.view.carousel.ProductCarouselUiView
import com.tokopedia.play.util.CachedState
import com.tokopedia.play.util.isChanged
import com.tokopedia.play.util.isNotChanged
import com.tokopedia.play.view.fragment.PlayUserInteractionFragment
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.state.PlayViewerNewUiState
import com.tokopedia.play_common.eventbus.EventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by kenny.hadisaputra on 06/07/22
 */
class ProductCarouselUiComponent(
    binding: ViewProductFeaturedBinding,
    private val bus: EventBus<Any>,
    scope: CoroutineScope,
) : UiComponent<PlayViewerNewUiState> {

    private val uiView = ProductCarouselUiView(
        binding, object : ProductCarouselUiView.Listener {
            override fun onProductImpressed(
                view: ProductCarouselUiView,
                productMap: Map<PlayProductUiModel.Product, Int>
            ) {
                bus.emit(Event.OnImpressed(productMap))
            }

            override fun onProductClicked(
                view: ProductCarouselUiView,
                product: PlayProductUiModel.Product,
                position: Int
            ) {
                bus.emit(Event.OnClicked(product, position))
            }

            override fun onTransactionClicked(
                view: ProductCarouselUiView,
                product: PlayProductUiModel.Product,
                action: ProductAction
            ) {
                bus.emit(Event.OnTransactionClicked(product, action))
            }
        }
    )

    init {
        scope.launch {
            bus.subscribe().collect {
                when (it) {
                    is PlayUserInteractionFragment.Event.OnFadingEdgeMeasured -> {
                        uiView.setFadingEndBounds(it.widthFromEnd)
                    }
                    PlayUserInteractionFragment.Event.OnScrubStarted -> uiView.setTransparent(true)
                    PlayUserInteractionFragment.Event.OnScrubEnded -> uiView.setTransparent(false)
                }
            }
        }
    }

    override fun render(state: CachedState<PlayViewerNewUiState>) {
        if (state.isNotChanged(
                { it.tagItems },
                { it.bottomInsets },
                { it.status.channelStatus.statusType },
                { it.featuredProducts },
                { it.address })) return

        val tagItems = state.value.tagItems

        if (tagItems.resultState.isLoading && state.value.featuredProducts.isEmpty()) {
            uiView.setLoading()
        } else if (state.isChanged { it.featuredProducts }) {
            uiView.setProducts(state.value.featuredProducts)

            val oldPinnedProduct = state.prevValue?.featuredProducts?.firstOrNull()?.takeIf { it.isPinned }
            val newPinnedProduct = state.value.featuredProducts.firstOrNull()?.takeIf { it.isPinned }

            if (newPinnedProduct != oldPinnedProduct && newPinnedProduct != null) {
                uiView.scrollToFirstPosition()
            }
        }

        if (!tagItems.resultState.isLoading && state.value.featuredProducts.isEmpty()) {
            uiView.hide()
        } else if (tagItems.product.canShow &&
            !state.value.bottomInsets.isAnyShown &&
            !tagItems.resultState.isFail &&
            state.value.status.channelStatus.statusType.isActive &&
            state.value.featuredProducts.isNotEmpty() &&
            !state.value.address.shouldShow
        ) uiView.show()
        else uiView.hide()

        if(state.isChanged { it.tagItems }) {
            bus.emit(Event.OnUpdated(uiView.getVisibleProducts()))
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> uiView.cleanUp()
            else -> {}
        }
    }

    sealed interface Event {
        data class OnImpressed(val productMap: Map<PlayProductUiModel.Product, Int>) : Event
        data class OnUpdated(val productMap: Map<PlayProductUiModel.Product, Int>) : Event
        data class OnClicked(val product: PlayProductUiModel.Product, val position: Int) : Event

        data class OnTransactionClicked(val product: PlayProductUiModel.Product, val action: ProductAction) : Event
    }
}
