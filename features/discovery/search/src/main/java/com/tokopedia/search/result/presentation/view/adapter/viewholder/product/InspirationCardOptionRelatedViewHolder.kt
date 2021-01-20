package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.InspirationCardOptionViewModel
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

    fun bind(inspirationCardOptionViewModel: InspirationCardOptionViewModel) {
        val container = itemView.findViewById<LinearLayout?>(R.id.inspirationCardRelatedContainer)
        val image = itemView.findViewById<ImageUnify?>(R.id.inspirationCardRelatedImage)
        val title = itemView.findViewById<Typography?>(R.id.inspirationCardRelatedTitle)

        image?.urlSrc = inspirationCardOptionViewModel.img
        title?.text = inspirationCardOptionViewModel.text

        container?.setOnClickListener {
            inspirationCardListener.onInspirationCardOptionClicked(inspirationCardOptionViewModel)
        }
    }
}