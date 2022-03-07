package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewDigitalHomeSearchHeaderItemBinding
import com.tokopedia.digital.home.old.model.DigitalHomePageSearchCategoryModel

class DigitalHomePageSearchHeaderViewHolder (itemView: View?) :
        AbstractViewHolder<DigitalHomePageSearchCategoryModel>(itemView) {

    override fun bind(element: DigitalHomePageSearchCategoryModel) {
        val bind = ViewDigitalHomeSearchHeaderItemBinding.bind(itemView)
        with(bind){
            tgSearchHeader.text = element.label
        }
    }

    companion object {
       val LAYOUT = R.layout.view_digital_home_search_header_item
    }
}