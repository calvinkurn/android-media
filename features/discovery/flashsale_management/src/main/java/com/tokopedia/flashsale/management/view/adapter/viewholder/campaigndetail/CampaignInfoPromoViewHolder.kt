package com.tokopedia.flashsale.management.view.adapter.viewholder.campaigndetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.view.viewmodel.CampaignInfoPromoViewModel
import kotlinx.android.synthetic.main.item_flash_sale_info_promo.view.*
import com.tokopedia.abstraction.common.utils.view.MethodChecker

class CampaignInfoPromoViewHolder(view: View): AbstractViewHolder<CampaignInfoPromoViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_flash_sale_info_promo
    }

    override fun bind(element: CampaignInfoPromoViewModel) {
        with(itemView){
            min_transaction.text = element.minTransaction
            promo_code.text = element.promoCode
            promo_title.setCompoundDrawablesWithIntrinsicBounds(null, null, MethodChecker.getDrawable
            (promo_title.context, R.drawable.info_icon), null)
        }
    }
}