package com.tokopedia.gamification.giftbox.presentation.dialogs

import android.content.Context
import com.tokopedia.dialog.DialogUnify

class TapTapExitDialog {

    fun show(context: Context, imageUrl: String, title: String, description: String, primaryCtaText: String, secondaryCtaText: String) {
        val dialog = DialogUnify(context, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setImageUrl(imageUrl)
        dialog.setTitle(title)
        dialog.setTitle(description)
        dialog.setPrimaryCTAText(primaryCtaText)
        dialog.setPrimaryCTAText(secondaryCtaText)
        dialog.setCancelable(false)
        dialog.setOverlayClose(false)
        dialog.show()
    }
}