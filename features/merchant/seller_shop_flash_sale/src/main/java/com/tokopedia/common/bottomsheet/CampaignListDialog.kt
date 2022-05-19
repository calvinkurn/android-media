package com.tokopedia.common.bottomsheet

import android.content.Context
import com.tokopedia.dialog.DialogUnify

fun showNoCampaignQuotaDialog(ctx: Context, primaryCTAAction: () -> Unit){
    val dialog = DialogUnify(ctx, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
    dialog.setTitle("Yaah, kuota campaign-mu habis")
    dialog.setDescription("Dapatkan tambahan kuota campaign dengan upgrade keanggotaan tokomu dulu, ya!")
    dialog.setPrimaryCTAText("Cek Keanggotaan Toko")
    dialog.setSecondaryCTAText("Kembali")

    dialog.setPrimaryCTAClickListener {
        primaryCTAAction.invoke()
    }

    dialog.setSecondaryCTAClickListener {
        dialog.dismiss()
    }
    dialog.show()
}