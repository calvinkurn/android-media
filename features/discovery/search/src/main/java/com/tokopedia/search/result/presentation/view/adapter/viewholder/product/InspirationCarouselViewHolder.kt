package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.graphics.Rect
import android.view.View
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID_BANNER
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel
import com.tokopedia.search.result.presentation.view.adapter.InspirationCarouselOptionAdapter
import com.tokopedia.search.result.presentation.view.adapter.InspirationCarouselOptionAdapterTypeFactory
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener
import com.tokopedia.search.utils.getHorizontalShadowOffset
import com.tokopedia.search.utils.getVerticalShadowOffset
import kotlinx.android.synthetic.main.search_inspiration_carousel.view.*
import kotlinx.coroutines.*

class InspirationCarouselViewHolder(
        itemView: View,
        private val inspirationCarouselListener: InspirationCarouselListener
) : AbstractViewHolder<InspirationCarouselViewModel>(itemView), CoroutineScope {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_inspiration_carousel
    }

    private val masterJob = SupervisorJob()
    override val coroutineContext = masterJob + Dispatchers.Main

    override fun onViewRecycled() {
        cancelJobs()
        super.onViewRecycled()
    }

    private fun cancelJobs() {
        if (isActive && !masterJob.isCancelled) {
            masterJob.children.map { it.cancel() }
        }
    }

    override fun bind(element: InspirationCarouselViewModel) {
        bindTitle(element)
        bindContent(element)
    }

    private fun bindTitle(element: InspirationCarouselViewModel) {
        itemView.inspirationCarousel?.inspirationCarouselTitle?.text = element.title
    }

    private fun bindContent(element: InspirationCarouselViewModel) {
        itemView.inspirationCarousel?.inspirationCarouselOptionList?.let {
            if (it.itemDecorationCount == 0) it.addItemDecoration(createItemDecoration())

            if (element.layout == LAYOUT_INSPIRATION_CAROUSEL_GRID) {
                val option = element.options.getOrNull(0) ?: return
                val productList = option.product.map{ product -> product.toProductCardModel() }
                it.initRecyclerViewForGrid(option, productList)
                configureSeeAllButton(option)
            } else {
                it.layoutManager = createLayoutManager()
                it.adapter = createAdapter(element.options)
            }
        }
    }
    private fun createGridProductList(option: InspirationCarouselViewModel.Option): List<Visitable<*>> {
        val list = mutableListOf<Visitable<*>>()
        list.add(createBannerOption(option))
        list.addAll(option.product)
        return list
    }

    private fun createBannerOption(option: InspirationCarouselViewModel.Option): InspirationCarouselViewModel.Option {
        return InspirationCarouselViewModel.Option(
                title = option.title,
                layout = LAYOUT_INSPIRATION_CAROUSEL_GRID_BANNER,
                bannerImageUrl = option.bannerImageUrl,
                bannerLinkUrl = option.bannerLinkUrl,
                bannerApplinkUrl = option.bannerApplinkUrl
        )
    }

    private fun RecyclerView.initRecyclerViewForGrid(option: InspirationCarouselViewModel.Option, productList: List<ProductCardModel>) {
        launch {
            try {
                layoutManager = createLayoutManager()
                adapter = createAdapter(createGridProductList(option))
                setHeightBasedOnProductCardMaxHeight(productList)
            }
            catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    private suspend fun RecyclerView.setHeightBasedOnProductCardMaxHeight(
            list: List<ProductCardModel>
    ) {
        val productCardHeight = getProductCardMaxHeight(list)
        val carouselLayoutParams = layoutParams
        carouselLayoutParams?.height = productCardHeight
        layoutParams = carouselLayoutParams
    }

    private fun InspirationCarouselViewModel.Option.Product.toProductCardModel(): ProductCardModel {
        return ProductCardModel(
                productImageUrl = imgUrl,
                productName = name,
                formattedPrice = priceStr,
                ratingCount = rating,
                reviewCount = countReview,
                slashedPrice = if (discountPercentage > 0) originalPrice else "",
                discountPercentage = if (discountPercentage > 0) "$discountPercentage%" else ""
        )
    }


    private suspend fun getProductCardMaxHeight(list: List<ProductCardModel>): Int {
        val productCardWidth = itemView.context.resources.getDimensionPixelSize(R.dimen.inspiration_carousel_grid_product_card_grid_width)
        return list.getMaxHeightForGridView(itemView.context, Dispatchers.Default, productCardWidth)
    }

    private fun configureSeeAllButton(option: InspirationCarouselViewModel.Option) {
        showSeeAllButton()
        bindSeeAllButtonListener(option)
    }

    private fun showSeeAllButton() {
        itemView.inspirationCarouselSeeAllButton?.visibility = View.VISIBLE
    }

    private fun bindSeeAllButtonListener(option: InspirationCarouselViewModel.Option) {
        itemView.inspirationCarouselSeeAllButton?.setOnClickListener {
            inspirationCarouselListener.onInspirationCarouselSeeAllClicked(option)
        }
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
    }

    private fun createAdapter(
            list: List<Visitable<*>>
    ): RecyclerView.Adapter<AbstractViewHolder<*>> {
        val typeFactory = InspirationCarouselOptionAdapterTypeFactory(inspirationCarouselListener)
        val inspirationCarouselProductAdapter = InspirationCarouselOptionAdapter(typeFactory)
        inspirationCarouselProductAdapter.clearData()
        inspirationCarouselProductAdapter.addAll(list)

        return inspirationCarouselProductAdapter
    }

    private fun createItemDecoration(): RecyclerView.ItemDecoration {
        return InspirationCarouselItemDecoration(
                itemView.context?.resources?.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16) ?: 0,
                itemView.context?.resources?.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_12) ?: 0,
                itemView.context?.resources?.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16) ?: 0,
                itemView.context?.resources?.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16) ?: 0
        )
    }

    private class InspirationCarouselItemDecoration(
            private val left: Int,
            private val top: Int,
            private val right: Int,
            private val bottom: Int
    ): RecyclerView.ItemDecoration() {

        private var cardViewHorizontalOffset = 0
        private var cardViewVerticalOffset = 0

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            if (view is CardView) setCardViewOffsets(view)
            else if (view is ProductCardGridView) setProductCardViewOffsets(view)

            setOutRectOffSetForCardView(outRect, view, parent)
        }

        private fun setCardViewOffsets(cardView: CardView) {
            cardViewHorizontalOffset = cardView.getHorizontalShadowOffset()
            cardViewVerticalOffset = cardView.getVerticalShadowOffset()
        }

        private fun setProductCardViewOffsets(view: ProductCardGridView) {
            cardViewHorizontalOffset = view.getHorizontalShadowOffset()
            cardViewVerticalOffset = view.getVerticalShadowOffset()
        }

        private fun setOutRectOffSetForCardView(outRect: Rect, view: View, parent: RecyclerView) {
            outRect.left = getLeftOffset(view, parent)
            outRect.right = getRightOffset(view, parent)
        }

        private fun getLeftOffset(view: View, parent: RecyclerView): Int {
            return if (parent.getChildAdapterPosition(view) == 0) getLeftOffsetFirstItem()
            else getLeftOffsetNotFirstItem()
        }

        private fun getLeftOffsetFirstItem(): Int { return left - (cardViewHorizontalOffset / 2) }

        private fun getLeftOffsetNotFirstItem(): Int { return (left / 4) - (cardViewHorizontalOffset / 2) }

        private fun getRightOffset(view: View, parent: RecyclerView): Int {
            return if (parent.getChildAdapterPosition(view) == (parent.adapter?.itemCount ?: 0) - 1) getRightOffsetLastItem()
            else getRightOffsetNotLastItem()
        }

        private fun getRightOffsetLastItem(): Int { return right - (cardViewHorizontalOffset / 2) }

        private fun getRightOffsetNotLastItem(): Int { return (right / 4) - (cardViewHorizontalOffset / 2) }
    }
}