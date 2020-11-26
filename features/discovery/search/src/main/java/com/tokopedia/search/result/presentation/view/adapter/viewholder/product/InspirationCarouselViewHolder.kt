package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.graphics.Rect
import android.view.View
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
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
            it.adapter = createAdapter(element.options)

            if (it.itemDecorationCount == 0) {
                it.addItemDecoration(createItemDecoration())
            }
        }
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
            if (view is CardView) {
                setCardViewOffsets(view)
                setOutRectOffSetForCardView(outRect, view, parent)
            }
        }

        private fun setCardViewOffsets(cardView: CardView) {
            cardViewHorizontalOffset = cardView.getHorizontalShadowOffset()
            cardViewVerticalOffset = cardView.getVerticalShadowOffset()
        }

        private fun setOutRectOffSetForCardView(outRect: Rect, cardView: CardView, parent: RecyclerView) {
            outRect.left = getLeftOffset(cardView, parent)
            outRect.top = getTopOffset()
            outRect.right = getRightOffset(cardView, parent)
            outRect.bottom = getBottomOffset()
        }

        private fun getLeftOffset(cardView: CardView, parent: RecyclerView): Int {
            return if (parent.getChildAdapterPosition(cardView) == 0) {
                left - (cardViewHorizontalOffset / 2)
            } else {
                (left / 4) - (cardViewHorizontalOffset / 2)
            }
        }

        private fun getTopOffset(): Int {
            return top - cardViewVerticalOffset
        }

        private fun getRightOffset(cardView: CardView, parent: RecyclerView): Int {
            return if (parent.getChildAdapterPosition(cardView) == (parent.adapter?.itemCount ?: 0) - 1) {
                right - (cardViewHorizontalOffset / 2)
            } else {
                (right / 4) - (cardViewHorizontalOffset / 2)
            }
        }

        private fun getBottomOffset(): Int {
            return bottom - (cardViewVerticalOffset / 2)
        }
    }
}