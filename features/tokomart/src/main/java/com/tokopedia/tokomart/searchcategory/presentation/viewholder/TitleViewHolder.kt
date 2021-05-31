package com.tokopedia.tokomart.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.searchcategory.presentation.listener.TitleListener
import com.tokopedia.tokomart.searchcategory.presentation.model.TitleDataView
import com.tokopedia.unifyprinciples.Typography

class TitleViewHolder(
        itemView: View,
        private val titleListener: TitleListener
): AbstractViewHolder<TitleDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_search_category_title
    }

    private var titleText: Typography? = null
    private var seeAllCategory: Typography? = null

    init {
        titleText = itemView.findViewById<Typography?>(R.id.tokomartSearchCategoryTitle)
        seeAllCategory = itemView.findViewById<Typography?>(R.id.tokomartSearchCategorySeeAllCategory)
    }

    override fun bind(element: TitleDataView?) {
        element ?: return

        titleText?.text = getTitle(element)
        seeAllCategory?.shouldShowWithAction(element.title.isNotEmpty()) {
            seeAllCategory?.setOnClickListener {
                titleListener.onSeeAllCategoryClicked()
            }
        }
    }

    private fun getTitle(element: TitleDataView) =
            if (element.title.isNotEmpty()) element.title
            else getString(R.string.tokomart_search_title)
}