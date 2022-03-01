package com.tokopedia.tokopedianow.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.SEARCH_CATEGORY_SUBTITLE_RESOURCE_ID
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.getServiceTypeFormattedCopy
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowSearchCategoryTitleBinding
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.TitleListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.AllProductTitle
import com.tokopedia.tokopedianow.searchcategory.presentation.model.CategoryTitle
import com.tokopedia.tokopedianow.searchcategory.presentation.model.SearchTitle
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TitleDataView
import com.tokopedia.utils.view.binding.viewBinding

class TitleViewHolder(
        itemView: View,
        private val titleListener: TitleListener
): AbstractViewHolder<TitleDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_search_category_title
    }

    private var binding: ItemTokopedianowSearchCategoryTitleBinding? by viewBinding()

    override fun bind(element: TitleDataView?) {
        element ?: return

        setTitle(element)
        setSeeAllCategory(element)
        setSubtitle(element)
    }

    private fun setTitle(element: TitleDataView) {
        binding?.tokoNowSearchCategoryTitle?.text = getTitle(element)
    }

    private fun setSeeAllCategory(element: TitleDataView) {
        binding?.apply {
            tokoNowSearchCategorySeeAllCategory.shouldShowWithAction(element.hasSeeAllCategoryButton) {
                tokoNowSearchCategorySeeAllCategory.setOnClickListener {
                    titleListener.onSeeAllCategoryClicked()
                }
            }
        }
    }

    private fun setSubtitle(element: TitleDataView) {
        binding?.apply {
            tokoNowSearchCategorySubtitle.shouldShowWithAction(element.is15mAvailable) {
                tokoNowSearchCategorySubtitle.text = getServiceTypeFormattedCopy(
                    context = root.context,
                    key = SEARCH_CATEGORY_SUBTITLE_RESOURCE_ID,
                    serviceType = element.serviceType
                )
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