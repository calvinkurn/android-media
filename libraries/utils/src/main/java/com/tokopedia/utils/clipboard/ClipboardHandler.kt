package com.tokopedia.utils.clipboard

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.tokopedia.config.GlobalConfig

object ClipboardHandler {
    fun copyToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Activity.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(GlobalConfig.PACKAGE_APPLICATION, text)
        clipboard.setPrimaryClip(clip)
    }
}
