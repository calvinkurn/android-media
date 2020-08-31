package com.tokopedia.feedcomponent.util.util

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager

/**
 * @author by yfsx on 17/05/19.
 */
class ClipboardHandler {

    fun copyToClipboard(context: Activity, Text: String) {
        val clipboard = context.getSystemService(Activity.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Tokopedia", Text)
        clipboard.setPrimaryClip(clip)
    }

}