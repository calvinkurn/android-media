package com.tokopedia.flashsale.management.view.adapter.viewholder.campaigndetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.ekstension.gone
import com.tokopedia.flashsale.management.ekstension.visible
import com.tokopedia.flashsale.management.view.viewmodel.CampaignInfoTnCViewModel
import kotlinx.android.synthetic.main.item_flash_sale_info_tnc.view.*

class CampaignInfoTnCViewHolder(view: View): AbstractViewHolder<CampaignInfoTnCViewModel>(view) {

    override fun bind(element: CampaignInfoTnCViewModel) {
        with(itemView) {
            val spanned = MethodChecker.fromHtml(element.tnc).toString()
            if (spanned.length > MAX_LENGTH) {
                tnc_content.text = spanned.substring(0, MAX_LENGTH)
                see_full_tnc.setOnClickListener {
                    tnc_content.text = spanned
                    see_full_tnc.gone()
                }
                see_full_tnc.visible()
            } else {
                tnc_content.text = spanned
                see_full_tnc.gone()
            }

        }
    }

    companion object {
        val LAYOUT = R.layout.item_flash_sale_info_tnc
        private const val MAX_LENGTH = 300
    }
}