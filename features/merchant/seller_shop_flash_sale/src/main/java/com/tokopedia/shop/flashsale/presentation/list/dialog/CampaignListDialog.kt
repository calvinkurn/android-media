package com.tokopedia.shop.flashsale.presentation.list.dialog

import android.annotation.SuppressLint
import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.seller_shop_flash_sale.R

@SuppressLint("ResourcePackage")
fun showNoCampaignQuotaDialog(context: Context, primaryCTAAction: () -> Unit){
    val dialog = DialogUnify(context, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
    dialog.setTitle(context.getString(R.string.no_campaign_quota_title))
    dialog.setDescription(context.getString(R.string.no_campaign_quota_description))
    dialog.setPrimaryCTAText(context.getString(R.string.no_campaign_quota_primary_cta_text))
    dialog.setSecondaryCTAText(context.getString(R.string.no_campaign_quota_secondary_cta_text))

    dialog.setPrimaryCTAClickListener {
        primaryCTAAction.invoke()
    }
    dialog.setSecondaryCTAClickListener {
        dialog.dismiss()
    }
    dialog.show()
}