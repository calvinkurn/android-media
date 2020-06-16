package com.tokopedia.vouchercreation.common.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.StringDef
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(SocmedPackage.INSTAGRAM, SocmedPackage.WHATSAPP, SocmedPackage.LINE, SocmedPackage.TWITTER, SocmedPackage.MESSENGER, SocmedPackage.FACEBOOK)
annotation class SocmedPackage {
    companion object {
        const val INSTAGRAM = "com.instagram.android"
        const val WHATSAPP = "com.whatsapp"
        const val LINE = "jp.naver.line.android"
        const val TWITTER = "com.twitter.android"
        const val FACEBOOK = "com.facebook.katana"
        const val MESSENGER = "com.facebook.orca"
    }
}

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(SocmedClass.INSTAGRAM, SocmedClass.WHATSAPP, SocmedClass.LINE, SocmedClass.TWITTER, SocmedClass.FACEBOOK)
annotation class SocmedClass {
    companion object {
        const val WHATSAPP = "com.whatsapp.ContactPicker"
        const val FACEBOOK = "com.facebook.composer.shareintent.ImplicitShareIntentHandlerDefaultAlias"
        const val LINE = "jp.naver.line.android.activity.selectchat.SelectChatActivityLaunchActivity"
        const val TWITTER = "com.twitter.composer.ComposerShareActivity"
        const val INSTAGRAM = "com.instagram.share.handleractivity.ShareHandlerActivity"
    }
}

enum class Socmed(@SocmedPackage val packageString: String,
                  @SocmedClass val classString: String) {
    INSTAGRAM(SocmedPackage.INSTAGRAM, SocmedClass.INSTAGRAM),
    WHATSAPP(SocmedPackage.WHATSAPP, SocmedClass.WHATSAPP),
    LINE(SocmedPackage.LINE, SocmedClass.LINE),
    TWITTER(SocmedPackage.TWITTER, SocmedClass.TWITTER),
    FACEBOOK(SocmedPackage.FACEBOOK, SocmedClass.FACEBOOK)
}

object SharingUtil {

    private const val SHARE_TYPE_TEXT = "text/plain"
    private const val SHARE_TYPE_IMAGE = "image/*"

    private const val FILE_NAME_FORMAT = "mvc_%s.jpg"

    private const val VOUCHER_DIR = "topsellervoucher"

    private const val IMAGE_LABEL = "Image"

    private const val MAXIMUM_FILES_IN_FOLDER = 10

    private val voucherDirectoryPath by lazy {
        Environment.getExternalStorageDirectory().toString() + File.separator + VOUCHER_DIR + File.separator
    }

    fun copyTextToClipboard(context: Context, label: String, text: String) {
        val clipboard: ClipboardManager? = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        clipboard?.run {
            primaryClip = ClipData.newPlainText(label, text)
        }
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
                        val stringPath = saveImageToExternalStorage(resource)
                        when(socmed) {
                            Socmed.INSTAGRAM -> {
                                shareInstagramFeed(context, stringPath, messageString)
                            }
                            Socmed.TWITTER -> {
                                goToSocialMedia(socmed.packageString, context, stringPath, messageString)
                            }
                            else -> {
                                goToSocialMedia(socmed.packageString, socmed.classString, context, stringPath, messageString)
                            }
                        }
                        return false
                    }
                })
                .submit()

    }

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

    private fun goToSocialMedia(@SocmedPackage packageString: String,
                                @SocmedClass classString: String,
                                context: Context,
                                pathFile: String,
                                messageString: String? = null) {
        val intent = context.packageManager.getLaunchIntentForPackage(packageString)
        if (intent != null) {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = SHARE_TYPE_IMAGE
                setClassName(packageString, classString)
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                try {
                    putExtra(Intent.EXTRA_STREAM, Uri.parse(pathFile))
                    messageString?.run {
                        putExtra(Intent.EXTRA_TEXT, this)
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
            context.startActivity(shareIntent)
        }
        else {
            // This block is reserved to handle unavailability of the socmed app.
            // Generally, this block should send intent to go to desired socmed app Play Store Url, but it isn't needed for now
        }
    }

    private fun shareInstagramFeed(context: Context,
                                   pathFile: String,
                                   messageString: String? = null) {

        val intent = context.packageManager.getLaunchIntentForPackage(SocmedPackage.INSTAGRAM)
        if (intent != null) {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = SHARE_TYPE_IMAGE
                setClassName(SocmedPackage.INSTAGRAM, SocmedClass.INSTAGRAM)
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                try {
                    putExtra(Intent.EXTRA_STREAM, Uri.parse(pathFile))
                    val imageMediaStore = MediaStore.Images.Media.insertImage(
                            context.contentResolver,
                            pathFile,
                            messageString.toBlankOrString(),
                            messageString.toBlankOrString()
                    )
                    val instagramShareUri = Uri.parse(imageMediaStore)
                    val clipData = ClipData.newRawUri(IMAGE_LABEL, instagramShareUri)
                    setClipData(clipData)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
        else {
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