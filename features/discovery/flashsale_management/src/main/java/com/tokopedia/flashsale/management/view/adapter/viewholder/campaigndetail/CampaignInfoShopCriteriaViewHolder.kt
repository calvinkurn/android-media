package com.tokopedia.flashsale.management.view.adapter.viewholder.campaigndetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.ekstension.loadUrl
import com.tokopedia.flashsale.management.view.viewmodel.CampaignInfoShopCriteriaViewModel
import kotlinx.android.synthetic.main.item_flash_sale_info_shop_criteria.view.*

class CampaignInfoShopCriteriaViewHolder(view: View): AbstractViewHolder<CampaignInfoShopCriteriaViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_flash_sale_info_shop_criteria
    }

    override fun bind(element: CampaignInfoShopCriteriaViewModel) {
        with(itemView){
            shop_type.text = element.shopType
            shop_reputation_min.loadUrl(element.shopMinReputation, 0f)
            shop_success_transaction.text = context.getString(R.string.range_format, "${element.shopMinCancellationRate}%",
                    "${element.shopMaxCancellationRate}%")
            shop_courrier.text = element.courierNames.joinToString(separator = ", ")
        }
    }
}