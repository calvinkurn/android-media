package com.tokopedia.vouchercreation.common.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.StringDef
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.kotlin.extensions.view.toBlankOrString


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
        Glide.with(context)
                .asBitmap()
                .load(urlString)
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        val stringPath = resource?.getSavedImageDirPath(context, System.currentTimeMillis().toString()).toBlankOrString()
                        goToSocialMedia(packageString, context, stringPath, messageString)
                        return false
                    }
                })
                .submit()

    }

    private fun goToSocialMedia(@SocmedPackage packageString: String,
                                context: Context,
                                pathFile: String,
                                messageString: String? = null) {
        val intent = context.packageManager.getLaunchIntentForPackage(packageString)
        intent?.run {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                setPackage(packageString)
                try {
                    putExtra(Intent.EXTRA_STREAM, Uri.parse(pathFile))
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