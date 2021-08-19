package com.tokopedia.tokopedianow.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.TitleListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.AllProductTitle
import com.tokopedia.tokopedianow.searchcategory.presentation.model.CategoryTitle
import com.tokopedia.tokopedianow.searchcategory.presentation.model.SearchTitle
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TitleDataView
import com.tokopedia.unifyprinciples.Typography

class TitleViewHolder(
        itemView: View,
        private val titleListener: TitleListener
): AbstractViewHolder<TitleDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_search_category_title
    }

    private var titleText: Typography? = null
    private var seeAllCategory: Typography? = null

    init {
        titleText = itemView.findViewById<Typography?>(R.id.tokoNowSearchCategoryTitle)
        seeAllCategory = itemView.findViewById<Typography?>(R.id.tokoNowSearchCategorySeeAllCategory)
    }

    override fun bind(element: TitleDataView?) {
        element ?: return

        titleText?.text = getTitle(element)
        seeAllCategory?.shouldShowWithAction(element.hasSeeAllCategoryButton) {
            seeAllCategory?.setOnClickListener {
                titleListener.onSeeAllCategoryClicked()
            }
        }
    }

    private fun getTitle(element: TitleDataView) =
            when(val titleType = element.titleType) {
                is CategoryTitle -> titleType.categoryName
                is SearchTitle -> getString(R.string.tokopedianow_search_title)
                is AllProductTitle -> getString(R.string.tokopedianow_all_products)
            }
}