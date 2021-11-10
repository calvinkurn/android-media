package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewDigitalHomeSearchDoubleLineItemBinding
import com.tokopedia.digital.home.old.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.digital.home.presentation.util.RechargeHomepageSectionMapper.boldReverseSearchAutoComplete
import com.tokopedia.unifycomponents.ImageUnify


class DigitalHomePageSearchDoubleLineViewHolder (itemView: View?, private val onSearchDoubleLineClickListener: OnSearchDoubleLineClickListener) :
        AbstractViewHolder<DigitalHomePageSearchCategoryModel>(itemView) {

    override fun bind(element: DigitalHomePageSearchCategoryModel) {
        val bind = ViewDigitalHomeSearchDoubleLineItemBinding.bind(itemView)
        with(bind){
            imgDoubleLine.apply {
                type = ImageUnify.TYPE_CIRCLE
                setImageUrl(element.icon)
            }
            tgMainTitleDoubleLine.text = boldReverseSearchAutoComplete(element.label, element.searchQuery)
            tgMainDescDoubleLine.text = element.subtitle

            root.setOnClickListener { onSearchDoubleLineClickListener.onSearchDoubleLineClicked(element, adapterPosition) }
        }
    }

    companion object {
        val LAYOUT = R.layout.view_digital_home_search_double_line_item
    }

    interface OnSearchDoubleLineClickListener {
        fun onSearchDoubleLineClicked(category: DigitalHomePageSearchCategoryModel, position: Int)
    }
}