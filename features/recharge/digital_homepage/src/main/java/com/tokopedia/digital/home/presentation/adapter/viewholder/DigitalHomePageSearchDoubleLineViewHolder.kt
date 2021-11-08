package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewDigitalHomeSearchDoubleLineItemBinding
import com.tokopedia.digital.home.old.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.digital.home.presentation.util.RechargeHomepageSectionMapper.boldReverseSearchAutoComplete
import com.tokopedia.media.loader.loadImageCircle


class DigitalHomePageSearchDoubleLineViewHolder (itemView: View?) :
        AbstractViewHolder<DigitalHomePageSearchCategoryModel>(itemView) {

    override fun bind(element: DigitalHomePageSearchCategoryModel) {
        val bind = ViewDigitalHomeSearchDoubleLineItemBinding.bind(itemView)
        with(bind){
            imgDoubleLine.apply {
                loadImageCircle(element.icon)
            }
            tgMainTitleDoubleLine.text = boldReverseSearchAutoComplete(element.label, element.searchQuery)
            tgMainDescDoubleLine.text = element.subtitle
        }
    }

    companion object {
        val LAYOUT = R.layout.view_digital_home_search_double_line_item
    }
}