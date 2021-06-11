package com.tokopedia.tokomart.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.searchcategory.presentation.listener.EmptyProductListener
import com.tokopedia.tokomart.searchcategory.presentation.model.EmptyProductDataView
import com.tokopedia.unifycomponents.UnifyButton

class EmptyProductViewHolder(
        itemView: View,
        private val emptyProductListener: EmptyProductListener,
): AbstractViewHolder<EmptyProductDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_search_category_empty_product
    }

    private var globalSearchButton: UnifyButton? = null
    private var changeKeywordButton: UnifyButton? = null

    override fun bind(element: EmptyProductDataView?) {
        element ?: return

        globalSearchButton = itemView.findViewById(R.id.tokonowEmptyProductGlobalSearchButton)
        changeKeywordButton = itemView.findViewById(R.id.tokonowEmptyProductChangeKeyword)

        globalSearchButton?.setOnClickListener {
            emptyProductListener.onGoToGlobalSearch()
        }

        changeKeywordButton?.setOnClickListener {
            emptyProductListener.onChangeKeywordButtonClick()
        }
    }
}