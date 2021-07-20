package com.tokopedia.universal_sharing.view.bottomsheet

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager

class ClipboardHandler {

    fun copyToClipboard(context: Activity, Text: String) {
        val clipboard = context.getSystemService(Activity.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Tokopedia", Text)
        clipboard.setPrimaryClip(clip)
    }

}