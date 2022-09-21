package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeSearchCategoryItemBinding
import com.tokopedia.digital.home.presentation.listener.SearchAutoCompleteListener
import com.tokopedia.digital.home.presentation.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.digital.home.presentation.util.RechargeHomepageSectionMapper
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage

class DigitalHomePageSearchViewHolder(
    itemView: View?, private val onSearchCategoryClickListener: OnSearchCategoryClickListener,
    private val listener: SearchAutoCompleteListener
) :
    AbstractViewHolder<DigitalHomePageSearchCategoryModel>(itemView) {

    override fun bind(element: DigitalHomePageSearchCategoryModel) {
        val bind = ViewRechargeHomeSearchCategoryItemBinding.bind(itemView)
        with(bind) {
            digitalHomepageSearchCategoryImage.loadImage(element.icon)

            // Add search query shading to category name
            digitalHomepageSearchCategoryName.text =
                RechargeHomepageSectionMapper.boldReverseSearchAutoComplete(
                    element.label,
                    element.searchQuery
                )

            itemView.addOnImpressionListener(element) {
                if (!element.trackerUser.keyword.isNullOrEmpty()) {
                    listener.impressCategoryListener(element.trackerUser, element.trackerItem)
                }
            }

            root.setOnClickListener {
                if (!element.trackerUser.keyword.isNullOrEmpty()) {
                    listener.clickCategoryListener(
                        element,
                        element.trackerUser,
                        element.trackerItem
                    )
                } else {
                    onSearchCategoryClickListener.onSearchCategoryClicked(element, adapterPosition)
                }
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.view_recharge_home_search_category_item
    }

    interface OnSearchCategoryClickListener {
        fun onSearchCategoryClicked(category: DigitalHomePageSearchCategoryModel, position: Int)
    }
}