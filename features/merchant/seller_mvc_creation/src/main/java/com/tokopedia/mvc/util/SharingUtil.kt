package com.tokopedia.mvc.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.tokopedia.mvc.R
import com.tokopedia.mvc.common.broadcastchat.BroadCastChatWebViewActivity

object SharingUtil {
    fun copyTextToClipboard(context: Context, label: String, text: String) {
        val clipboard: ClipboardManager? = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        clipboard?.run {
            setPrimaryClip(ClipData.newPlainText(label, text))
        }
    }

    //TODO change this to Long
    fun shareToBroadCastChat(context: Context, voucherId: Long) {
        val broadCastChatUrl = "https://m.tokopedia.com/broadcast-chat/create/content?voucher_id=$voucherId"
        val broadCastChatIntent = BroadCastChatWebViewActivity.createNewIntent(
            context = context,
            url = broadCastChatUrl,
            title = context.getString(R.string.smvc_broadcast_chat_label)
        )
        context.startActivity(broadCastChatIntent)
    }
}
