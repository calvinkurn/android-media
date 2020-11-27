package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.graphics.Rect
import android.view.View
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID_BANNER
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel
import com.tokopedia.search.result.presentation.view.adapter.InspirationCarouselOptionAdapter
import com.tokopedia.search.result.presentation.view.adapter.InspirationCarouselOptionAdapterTypeFactory
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener
import com.tokopedia.search.utils.getHorizontalShadowOffset
import com.tokopedia.search.utils.getVerticalShadowOffset
import kotlinx.android.synthetic.main.search_inspiration_carousel.view.*

class InspirationCarouselViewHolder(
        itemView: View,
        private val inspirationCarouselListener: InspirationCarouselListener
) : AbstractViewHolder<InspirationCarouselViewModel>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_inspiration_carousel
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
            it.layoutManager = createLayoutManager()
            it.adapter = createAdapter(element.options.shouldAddGridBanner())

            if (it.itemDecorationCount == 0) {
                it.addItemDecoration(createItemDecoration())
            }
        }
    }

    private fun List<InspirationCarouselViewModel.Option>.shouldAddGridBanner(): List<InspirationCarouselViewModel.Option> {
        val option = getOrNull(0) ?: return this
        if (option.layout != LAYOUT_INSPIRATION_CAROUSEL_GRID) return this

        val data: MutableList<InspirationCarouselViewModel.Option> = mutableListOf()
        data.add(createBannerOption(option))
        data.addAll(this)
        return data
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

    private fun createLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
    }

    private fun createAdapter(
            inspirationCarouselOptionList: List<InspirationCarouselViewModel.Option>
    ): RecyclerView.Adapter<AbstractViewHolder<*>> {
        val typeFactory = InspirationCarouselOptionAdapterTypeFactory(inspirationCarouselListener)
        val inspirationCarouselProductAdapter = InspirationCarouselOptionAdapter(typeFactory)
        inspirationCarouselProductAdapter.clearData()
        inspirationCarouselProductAdapter.addAll(inspirationCarouselOptionList)

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
            outRect.top = getTopOffset()
            outRect.right = getRightOffset(view, parent)
            outRect.bottom = getBottomOffset()
        }

        private fun getLeftOffset(view: View, parent: RecyclerView): Int {
            return if (parent.getChildAdapterPosition(view) == 0) getLeftOffsetFirstItem()
            else getLeftOffsetNotFirstItem()
        }

        private fun getLeftOffsetFirstItem(): Int { return left - (cardViewHorizontalOffset / 2) }

        private fun getLeftOffsetNotFirstItem(): Int { return (left / 4) - (cardViewHorizontalOffset / 2) }

        private fun getTopOffset(): Int {
            return top - cardViewVerticalOffset
        }

        private fun getRightOffset(view: View, parent: RecyclerView): Int {
            return if (parent.getChildAdapterPosition(view) == (parent.adapter?.itemCount ?: 0) - 1) getRightOffsetLastItem()
            else getRightOffsetNotLastItem()
        }

        private fun getRightOffsetLastItem(): Int { return right - (cardViewHorizontalOffset / 2) }

        private fun getRightOffsetNotLastItem(): Int { return (right / 4) - (cardViewHorizontalOffset / 2) }

        private fun getBottomOffset(): Int {
            return bottom - (cardViewVerticalOffset / 2)
        }
    }
}