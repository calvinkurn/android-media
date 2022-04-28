package com.tokopedia.autocompletecomponent.suggestion.topshop

import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.View
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.SuggestionTopShopWidgetLayoutBinding
import com.tokopedia.autocompletecomponent.util.getHorizontalShadowOffset
import com.tokopedia.autocompletecomponent.util.getVerticalShadowOffset
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.utils.view.binding.viewBinding

class SuggestionTopShopWidgetViewHolder(
        itemView: View,
        private val suggestionTopShopListener: SuggestionTopShopListener
): AbstractViewHolder<SuggestionTopShopWidgetDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.suggestion_top_shop_widget_layout
    }
    private var binding : SuggestionTopShopWidgetLayoutBinding? by viewBinding()

    override fun bind(element: SuggestionTopShopWidgetDataView) {
        bindTitle(element)
        bindCards(element)
    }

    private fun bindTitle(element: SuggestionTopShopWidgetDataView) {
        binding?.suggestionTopShopTitle?.shouldShowWithAction(element.title.isNotEmpty()) {
            binding?.suggestionTopShopTitle?.text = element.title
        }
    }

    private fun bindCards(element: SuggestionTopShopWidgetDataView) {
        binding?.suggestionTopShopCards?.let {
            it.layoutManager = createLayoutManager()
            it.adapter = createAdapter(element.listSuggestionTopShopCardData)

            if (it.itemDecorationCount == 0) {
                it.addItemDecoration(createItemDecoration())
            }
        }
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
    }

    private fun createAdapter(
            topShopCardDataList: List<SuggestionTopShopCardDataView>
    ): RecyclerView.Adapter<AbstractViewHolder<*>> {
        val typeFactory = SuggestionTopShopAdapterTypeFactory(suggestionTopShopListener)
        val topShopCardAdapter = TopShopCardAdapter(typeFactory)
        topShopCardAdapter.addAll(topShopCardDataList)

        return topShopCardAdapter
    }

    private fun createItemDecoration(): RecyclerView.ItemDecoration {
        return TopShopCardItemDecoration(
                itemView.context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16) ?: 0,
                itemView.context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16) ?: 0,
                itemView.context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16) ?: 0,
                itemView.context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_4) ?: 0
        )
    }

    private class TopShopCardItemDecoration(
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

        @SuppressLint("MagicNumber")
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

        @SuppressLint("MagicNumber")
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