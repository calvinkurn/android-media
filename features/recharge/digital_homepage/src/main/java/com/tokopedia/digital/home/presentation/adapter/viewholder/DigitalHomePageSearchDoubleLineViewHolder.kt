package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewDigitalHomeSearchDoubleLineItemBinding
import com.tokopedia.digital.home.presentation.listener.SearchAutoCompleteListener
import com.tokopedia.digital.home.presentation.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.digital.home.presentation.util.RechargeHomepageSectionMapper.boldReverseSearchAutoComplete
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifycomponents.ImageUnify


class DigitalHomePageSearchDoubleLineViewHolder (itemView: View?,
                                                 private val onSearchDoubleLineClickListener: OnSearchDoubleLineClickListener,
                                                 private val listener: SearchAutoCompleteListener
) :
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

            itemView.addOnImpressionListener(element) {
                listener.impressOperatorListener(element.trackerUser, element.trackerItem)
            }

            root.setOnClickListener {
                listener.clickOperatorListener(element.trackerUser, element.trackerItem)
                onSearchDoubleLineClickListener.onSearchDoubleLineClicked(element, adapterPosition)
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.view_digital_home_search_double_line_item
    }

    interface OnSearchDoubleLineClickListener {
        fun onSearchDoubleLineClicked(category: DigitalHomePageSearchCategoryModel, position: Int)
    }
}