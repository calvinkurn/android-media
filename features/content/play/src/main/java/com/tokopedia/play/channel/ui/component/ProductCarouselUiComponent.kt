package com.tokopedia.play.channel.ui.component

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.databinding.ViewProductFeaturedBinding
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.ui.component.UiComponent
import com.tokopedia.play.ui.view.carousel.ProductCarouselUiView
import com.tokopedia.play.util.CachedState
import com.tokopedia.play.util.isChanged
import com.tokopedia.play.util.isNotChanged
import com.tokopedia.play.view.fragment.PlayUserInteractionFragment
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play.view.uimodel.state.PlayViewerNewUiState
import com.tokopedia.play_common.eventbus.EventBus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by kenny.hadisaputra on 06/07/22
 */
class ProductCarouselUiComponent(
    binding: ViewProductFeaturedBinding,
    bus: EventBus<Any>,
    private val scope: CoroutineScope,
    private val dispatchers: CoroutineDispatchers,
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

    private var setProductsJob: Job? = null

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

        setProductsJob?.cancel()
        setProductsJob = scope.launch(dispatchers.immediate) {
            if (tagItems.resultState.isLoading && tagItems.product.productSectionList.isEmpty()) {
                uiView.setLoading()
            } else if (state.isChanged { it.tagItems.product.productSectionList }) {
                uiView.setProducts(getFeaturedProducts(state.value.tagItems))
            }
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

    private suspend fun getFeaturedProducts(
        tagItems: TagItemUiModel,
    ): List<PlayProductUiModel.Product> = withContext(dispatchers.computation) {
        val pinnedProductSection = tagItems.product.productSectionList.firstOrNull {
            it is ProductSectionUiModel.Section && it.productList.any { product -> product.isPinned }
        }
        val pinnedProduct = (pinnedProductSection as? ProductSectionUiModel.Section)?.productList
            ?.first { it.isPinned }

        val featuredProducts = tagItems.product.productSectionList
            .filterIsInstance<ProductSectionUiModel.Section>()
            .flatMap { it.productList }
            .take(tagItems.maxFeatured)
            .filter { !it.isPinned }

        return@withContext if (pinnedProduct != null) listOf(pinnedProduct) + featuredProducts
        else featuredProducts
    }

    sealed interface Event {
        data class OnImpressed(val products: List<Pair<PlayProductUiModel.Product, Int>>) : Event
        data class OnClicked(val product: PlayProductUiModel.Product, val position: Int) : Event

        data class OnAtcClicked(val product: PlayProductUiModel.Product) : Event
        data class OnBuyClicked(val product: PlayProductUiModel.Product) : Event
    }
}