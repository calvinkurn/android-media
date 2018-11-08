package com.tokopedia.flashsale.management.view.adapter.viewholder.campaigndetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.view.viewmodel.CampaignInfoSectionViewModel
import kotlinx.android.synthetic.main.item_flash_sale_info_section.view.*

class CampaignInfoSectionViewHolder(val view: View): AbstractViewHolder<CampaignInfoSectionViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_flash_sale_info_section
    }

    override fun bind(element: CampaignInfoSectionViewModel) {
        itemView.title.text = element.title
    }
}