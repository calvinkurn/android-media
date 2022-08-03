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
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
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
                { it.status.channelStatus.statusType },
                { it.address })) return

        val tagItems = state.value.tagItems

        if (tagItems.resultState.isLoading && tagItems.product.productSectionList.isEmpty()) {
            uiView.setLoading()
        } else if (state.isChanged{ it.tagItems.product.productSectionList }) {
            val newPinnedProduct = getPinnedProduct(state.value.tagItems.product.productSectionList)
            uiView.setProducts(
                getFeaturedProducts(
                    state.value.tagItems.product.productSectionList,
                    state.value.tagItems.maxFeatured,
                    newPinnedProduct,
                )
            )

            val oldPinnedProduct = state.prevValue?.tagItems?.product?.productSectionList
                ?.let(::getPinnedProduct)
            if (newPinnedProduct != oldPinnedProduct && newPinnedProduct != null) {
                uiView.scrollToFirstPosition()
            }
        }

        if (!tagItems.resultState.isLoading && tagItems.product.productSectionList.isEmpty()) {
            uiView.hide()
        } else if (tagItems.product.canShow &&
            !state.value.bottomInsets.isAnyShown &&
            !tagItems.resultState.isFail &&
            state.value.status.channelStatus.statusType.isActive &&
            tagItems.product.productSectionList.isNotEmpty() &&
            !state.value.address.shouldShow
        ) uiView.show()
        else uiView.hide()
    }

    private fun getPinnedProduct(
        sectionList: List<ProductSectionUiModel>,
    ): PlayProductUiModel.Product? {
        var pinnedProduct: PlayProductUiModel.Product? = null
        run {
            sectionList.forEach { section ->
                if (section is ProductSectionUiModel.Section) {
                    pinnedProduct = section.productList.firstOrNull { it.isPinned }
                    if (pinnedProduct != null) return@run
                }
            }
        }

        return pinnedProduct
    }

    private fun getFeaturedProducts(
        sectionList: List<ProductSectionUiModel>,
        maxProducts: Int,
        pinnedProduct: PlayProductUiModel.Product?,
    ): List<PlayProductUiModel.Product> {
        val rawFeaturedProducts = getRawFeaturedProducts(sectionList, maxProducts)

        return pinnedProduct?.let { pinned ->
            listOf(pinned) + rawFeaturedProducts.filterNot { it.isPinned }
        } ?: rawFeaturedProducts
    }

    private fun getRawFeaturedProducts(
        sectionList: List<ProductSectionUiModel>,
        maxProducts: Int,
    ): List<PlayProductUiModel.Product> {
        val products = mutableListOf<PlayProductUiModel.Product>()
        sectionList.forEach { section ->
            if (section is ProductSectionUiModel.Section) {
                products.addAll(section.productList.take(maxProducts - products.size))
            }

            if (products.size >= maxProducts) return@forEach
        }

        return products
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> uiView.cleanUp()
            else -> {}
        }
    }

    sealed interface Event {
        data class OnImpressed(val productMap: Map<PlayProductUiModel.Product, Int>) : Event
        data class OnClicked(val product: PlayProductUiModel.Product, val position: Int) : Event

        data class OnAtcClicked(val product: PlayProductUiModel.Product) : Event
        data class OnBuyClicked(val product: PlayProductUiModel.Product) : Event
    }
}