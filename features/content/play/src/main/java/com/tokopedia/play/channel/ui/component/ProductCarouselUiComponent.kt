package com.tokopedia.play.channel.ui.component

import com.tokopedia.play.databinding.ViewProductFeaturedBinding
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.ui.component.UiComponent
import com.tokopedia.play.ui.view.carousel.ProductCarouselUiView
import com.tokopedia.play.util.CachedState
import com.tokopedia.play.util.isChanged
import com.tokopedia.play.util.isNotChanged
import com.tokopedia.play.view.fragment.PlayUserInteractionFragment
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
    bus: EventBus<Any>,
    scope: CoroutineScope,
) : UiComponent<PlayViewerNewUiState> {

    private val uiView = ProductCarouselUiView(
        binding,
        object : ProductCarouselUiView.Listener {
            override fun onProductImpressed(
                view: ProductCarouselUiView,
                products: List<Pair<PlayProductUiModel.Product, Int>>
            ) {
                bus.emit(Event.OnImpressed(products))
            }

            override fun onProductClicked(
                view: ProductCarouselUiView,
                product: PlayProductUiModel.Product,
                position: Int
            ) {
                bus.emit(Event.OnClicked(product, position))
            }

            override fun onAtcClicked(
                view: ProductCarouselUiView,
                product: PlayProductUiModel.Product
            ) {
                bus.emit(Event.OnAtcClicked(product))
            }

            override fun onBuyClicked(
                view: ProductCarouselUiView,
                product: PlayProductUiModel.Product
            ) {
                bus.emit(Event.OnBuyClicked(product))
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
                { it.tagItems },
                { it.status.channelStatus.statusType })) return

        val tagItems = state.value.tagItems

        if (tagItems.resultState.isLoading && tagItems.product.productSectionList.isEmpty()) {
            uiView.setLoading()
        } else if (state.isChanged { it.tagItems.product.productSectionList }) {
            uiView.setProducts(
                tagItems.product.productSectionList,
                tagItems.maxFeatured,
            )
        }

        if (!tagItems.resultState.isLoading && tagItems.product.productSectionList.isEmpty()) {
            uiView.hide()
        } else if (tagItems.product.canShow &&
            !state.value.bottomInsets.isAnyShown &&
            !tagItems.resultState.isFail &&
            state.value.status.channelStatus.statusType.isActive &&
            tagItems.product.productSectionList.isNotEmpty()
        ) uiView.show()
        else uiView.hide()
    }

    sealed interface Event {
        data class OnImpressed(val products: List<Pair<PlayProductUiModel.Product, Int>>) : Event
        data class OnClicked(val product: PlayProductUiModel.Product, val position: Int) : Event

        data class OnAtcClicked(val product: PlayProductUiModel.Product) : Event
        data class OnBuyClicked(val product: PlayProductUiModel.Product) : Event
    }
}