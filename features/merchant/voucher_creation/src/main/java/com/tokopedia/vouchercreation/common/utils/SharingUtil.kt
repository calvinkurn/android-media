package com.tokopedia.vouchercreation.common.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.StringDef


@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(SocmedPackage.INSTAGRAM, SocmedPackage.WHATSAPP, SocmedPackage.LINE, SocmedPackage.TWITTER, SocmedPackage.MESSENGER)
annotation class SocmedPackage {
    companion object {
        const val INSTAGRAM = "com.instagram.android"
        const val WHATSAPP = "com.whatsapp"
        const val LINE = "jp.naver.line.android"
        const val TWITTER = "com.twitter.android"
        const val MESSENGER = "com.facebook.orca"
    }
}

object SharingUtil {

    private const val SHARE_TYPE_TEXT = "text/plain"
    private const val SHARE_TYPE_IMAGE = "image/jpeg"

    fun copyTextToClipboard(context: Context, label: String, text: String) {
        val clipboard: ClipboardManager? = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        clipboard?.run {
            primaryClip = ClipData.newPlainText(label, text)
        }
    }

    fun shareToSocialMedia(@SocmedPackage packageString: String,
                           context: Context,
                           urlString: String,
                           messageString: String? = null) {
        val intent = context.packageManager.getLaunchIntentForPackage(packageString)
        intent?.run {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                setPackage(packageString)
                try {
                    putExtra(Intent.EXTRA_STREAM, Uri.parse(urlString))
                    messageString?.run {
                        putExtra(Intent.EXTRA_TEXT, this)
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                type = SHARE_TYPE_IMAGE
            }
            context.startActivity(shareIntent)
        }
    }

    fun otherShare(context: Context, messageShare: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = SHARE_TYPE_TEXT
            putExtra(Intent.EXTRA_TEXT, messageShare)
        }
        context.startActivity(intent)
    }

}