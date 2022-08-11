package com.tokopedia.shop.flashsale.presentation.creation.manage.dialog

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.seller_shop_flash_sale.R

fun showSuccessSaveCampaignDraft(context: Context, campaignName: String, action: () -> Unit){
    val dialog = DialogUnify(context, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
    dialog.setTitle(context.getString(R.string.success_register_campaign_title, campaignName))
    dialog.setDescription(context.getString(R.string.success_register_campaign_description))
    dialog.setPrimaryCTAText(context.getString(R.string.success_register_campaign_primary_cta_text))

    dialog.setPrimaryCTAClickListener {
        dialog.dismiss()
        action.invoke()
    }
    dialog.show()
}