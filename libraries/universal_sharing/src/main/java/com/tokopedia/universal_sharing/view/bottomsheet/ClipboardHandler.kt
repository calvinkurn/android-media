package com.tokopedia.universal_sharing.view.bottomsheet

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager

class ClipboardHandler {

    val LABEL_TOKOPEDIA = "Tokopedia"

    fun copyToClipboard(context: Activity, Text: String) {
        val clipboard = context.getSystemService(Activity.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(LABEL_TOKOPEDIA, Text)
        clipboard.setPrimaryClip(clip)
    }

}