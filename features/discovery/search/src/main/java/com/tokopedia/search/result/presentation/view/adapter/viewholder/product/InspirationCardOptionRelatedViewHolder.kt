package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.InspirationCardOptionDataView
import com.tokopedia.search.result.presentation.view.listener.InspirationCardListener
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class InspirationCardOptionRelatedViewHolder(
        itemView: View,
        private val inspirationCardListener: InspirationCardListener
): RecyclerView.ViewHolder(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_result_product_inspiration_card_option_related_layout
    }

    fun bind(inspirationCardOptionDataView: InspirationCardOptionDataView, spanCount: Int) {
        val container = itemView.findViewById<ConstraintLayout?>(R.id.inspirationCardRelatedContainer)
        val image = itemView.findViewById<ImageUnify?>(R.id.inspirationCardRelatedImage)
        val title = itemView.findViewById<Typography?>(R.id.inspirationCardRelatedTitle)
        val separator = itemView.findViewById<View?>(R.id.inspirationCardSeparator)

        image?.urlSrc = inspirationCardOptionDataView.img
        title?.text = inspirationCardOptionDataView.text

        val isTopOfRecyclerView = adapterPosition < spanCount
        separator?.showWithCondition(!isTopOfRecyclerView)

        container?.setOnClickListener {
            inspirationCardListener.onInspirationCardOptionClicked(inspirationCardOptionDataView)
        }
    }
}