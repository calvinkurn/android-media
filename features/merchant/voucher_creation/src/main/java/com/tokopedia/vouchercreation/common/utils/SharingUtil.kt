package com.tokopedia.vouchercreation.common.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.annotation.StringDef
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.view.activity.BroadCastChatWebViewActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(SocmedPackage.INSTAGRAM, SocmedPackage.WHATSAPP, SocmedPackage.LINE, SocmedPackage.TWITTER, SocmedPackage.FACEBOOK)
annotation class SocmedPackage {
    companion object {
        const val INSTAGRAM = "com.instagram.android"
        const val WHATSAPP = "com.whatsapp"
        const val LINE = "jp.naver.line.android"
        const val TWITTER = "com.twitter.android"
        const val FACEBOOK = "com.facebook.katana"
    }
}

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(SocmedClass.WHATSAPP, SocmedClass.FACEBOOK)
annotation class SocmedClass {
    companion object {
        const val WHATSAPP = "com.whatsapp.ContactPicker"
        const val FACEBOOK = "com.facebook.composer.shareintent.ImplicitShareIntentHandlerDefaultAlias"
    }
}

enum class Socmed(@SocmedPackage val packageString: String,
                  @SocmedClass val classString: String) {
    INSTAGRAM(SocmedPackage.INSTAGRAM, ""),
    LINE(SocmedPackage.LINE, ""),
    TWITTER(SocmedPackage.TWITTER, ""),
    WHATSAPP(SocmedPackage.WHATSAPP, SocmedClass.WHATSAPP),
    FACEBOOK(SocmedPackage.FACEBOOK, SocmedClass.FACEBOOK)
}

object SharingUtil {

    private const val SHARE_TYPE_TEXT = "text/plain"
    private const val SHARE_TYPE_IMAGE = "image/*"

    private const val FILE_NAME_FORMAT = "mvc_%s"

    private const val VOUCHER_DIR = "topsellervoucher"

    private const val MAXIMUM_FILES_IN_FOLDER = 10

    private const val AUTHORITY = "com.tokopedia.sellerapp.provider"

    @Deprecated("Use internal storage directory instead")
    private val voucherDirectoryPath by lazy {
        Environment.getExternalStorageDirectory().toString() + File.separator + VOUCHER_DIR + File.separator
    }

    fun copyTextToClipboard(context: Context, label: String, text: String) {
        val clipboard: ClipboardManager? = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        clipboard?.run {
            setPrimaryClip(ClipData.newPlainText(label, text))
        }
    }

    fun shareToBroadCastChat(context: Context, voucherId: Int) {
        val broadCastChatUrl = "https://m.tokopedia.com/broadcast-chat/create?voucher_id=$voucherId"
        val broadCastChatIntent = BroadCastChatWebViewActivity.createNewIntent(
                context = context,
                url = broadCastChatUrl,
                title = context.getString(R.string.mvc_broadcast_chat)
        )
        context.startActivity(broadCastChatIntent)
    }

    fun shareToSocialMedia(socmed: Socmed,
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

                    override fun onResourceReady(resource: Bitmap, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        val fileName = String.format(FILE_NAME_FORMAT, System.currentTimeMillis().toString())
                        val internalFile = resource.getSavedImageDirFile(context, fileName)
                        val contentUri = FileProvider.getUriForFile(context, AUTHORITY, internalFile)
                        when (socmed) {
                            Socmed.INSTAGRAM -> {
                                shareInstagramFeed(context, contentUri)
                            }
                            Socmed.TWITTER, Socmed.LINE -> {
                                goToSocialMedia(socmed.packageString, context, contentUri, messageString)
                            }
                            else -> {
                                goToSocialMedia(socmed.packageString, socmed.classString, context, contentUri, messageString)
                            }
                        }
                        return false
                    }
                })
                .submit()

    }

    @Deprecated("Please use getSavedImageDirFile to save images into internal storage as directly accessing external storage could impose security leaks")
    private fun saveImageToExternalStorage(imageBitmap: Bitmap): String {
        val fileName = String.format(FILE_NAME_FORMAT, System.currentTimeMillis().toString())
        checkVoucherDirectory()
        val filePath = File(voucherDirectoryPath, fileName)

        val fos = FileOutputStream(filePath)
        try {
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            try {
                fos.close()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
        return filePath.toString()
    }

    /**
     * Check if directory for saving voucher images is exist. If not, make the directory
     * Delete file(s) if there are already a lot of files inside the dir
     */
    @Deprecated("Use FileProviderUtil File.checkVoucherDirectory() instead")
    private fun checkVoucherDirectory() {
        val fileDir = File(voucherDirectoryPath)
        if (fileDir.exists() && fileDir.isDirectory) {
            val voucherFiles = fileDir.listFiles()
            voucherFiles?.run {
                if (size >= MAXIMUM_FILES_IN_FOLDER) {
                    get(0)?.delete()
                }
            }
        } else {
            fileDir.mkdir()
        }
    }

    private fun goToSocialMedia(@SocmedPackage packageString: String,
                                context: Context,
                                pathFile: Uri,
                                messageString: String? = null) {
        val intent = context.packageManager.getLaunchIntentForPackage(packageString)
        intent?.run {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                setPackage(packageString)
                try {
                    putExtra(Intent.EXTRA_STREAM, pathFile)
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

    private fun goToSocialMedia(@SocmedPackage packageString: String,
                                @SocmedClass classString: String,
                                context: Context,
                                pathFile: Uri,
                                messageString: String? = null) {
        val intent = context.packageManager.getLaunchIntentForPackage(packageString)
        if (intent != null) {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = SHARE_TYPE_IMAGE
                setClassName(packageString, classString)
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                try {
                    putExtra(Intent.EXTRA_STREAM, pathFile)
                    messageString?.run {
                        putExtra(Intent.EXTRA_TEXT, this)
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
            context.startActivity(shareIntent)
        } else {
            // This block is reserved to handle unavailability of the socmed app.
            // Generally, this block should send intent to go to desired socmed app Play Store Url, but it isn't needed for now
        }
    }

    private fun shareInstagramFeed(context: Context,
                                   fileUri: Uri) {

        val intent = context.packageManager.getLaunchIntentForPackage(SocmedPackage.INSTAGRAM)
        if (intent != null) {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = SHARE_TYPE_IMAGE
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                setPackage(SocmedPackage.INSTAGRAM)
                try {
                    putExtra(Intent.EXTRA_STREAM, fileUri)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
            context.startActivity(shareIntent)
        } else {
            // This block is reserved to handle unavailability of the socmed app.
            // Generally, this block should send intent to go to desired socmed app Play Store Url, but it isn't needed for now
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