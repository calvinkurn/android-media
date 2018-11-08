package com.tokopedia.flashsale.management.view.adapter.viewholder.campaigndetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.view.viewmodel.CampaignInfoPromoViewModel
import kotlinx.android.synthetic.main.item_flash_sale_info_promo.view.*

class CampaignInfoPromoViewHolder(view: View): AbstractViewHolder<CampaignInfoPromoViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_flash_sale_info_promo
    }

    override fun bind(element: CampaignInfoPromoViewModel) {
        with(itemView){
            min_transaction.text = element.minTransaction
            promo_code.text = element.promoCode
        }
    }
}