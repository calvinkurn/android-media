package com.tokopedia.data_explorer.db_explorer.extensions

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

object DbExplorerHelper {

    private val COPY_BOARD_LABEL = "Tokopedia"

    fun copyToClipBoard(context: Context, dataStr: String) {
        try {
            val extraSpaceRegexStr = "\\s+".toRegex()
            val clipboard = context.getSystemService(Activity.CLIPBOARD_SERVICE)
                    as ClipboardManager
            val clip = ClipData.newPlainText(
                COPY_BOARD_LABEL,
                dataStr.replace(extraSpaceRegexStr, "")
            )
            clipboard.setPrimaryClip(clip)
        } catch (e: Exception) {
        }
    }
}