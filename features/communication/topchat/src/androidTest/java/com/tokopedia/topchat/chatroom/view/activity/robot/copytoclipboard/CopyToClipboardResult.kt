package com.tokopedia.topchat.chatroom.view.activity.robot.copytoclipboard

import android.content.ClipboardManager
import android.content.Context

object CopyToClipboardResult {

    fun getClipboardMsg(context: Context): CharSequence? {
        val clipboard: ClipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        return clipboard.primaryClip?.getItemAt(0)?.text
    }
}
