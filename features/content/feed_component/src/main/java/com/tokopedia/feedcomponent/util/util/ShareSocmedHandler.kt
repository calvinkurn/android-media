package com.tokopedia.feedcomponent.util.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedcomponent.R
import com.tokopedia.user.session.UserSession
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 * @author by yfsx on 17/05/19.
 */
class ShareSocmedHandler(val activity: Activity) {
    private val PERMISSIONS = Arrays.asList("publish_actions")
    private lateinit var fbinterface: FacebookInterface

    interface FacebookInterface {
        fun onShareComplete()

        fun onShareFailed()
    }

    companion object {

        val EXCLUSION = "EXCLUSION"
    }

    fun shareData(context: Activity?, packageName: String, targetType: String, shareTxt: String, ProductUri: String, image: Bitmap?, altUrl: String?) {
        val share = Intent(Intent.ACTION_SEND).setType(targetType)
        if (image != null) {
            val bytes = ByteArrayOutputStream()
            checkTempDirectory()
            val file = File(Environment.getExternalStorageDirectory().toString() + File.separator + "tkpdtemp" + File.separator + uniqueCode() + ".jpg")
                    .apply { createNewFile() }
            image.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val fo = FileOutputStream(file)
            try {
                fo.write(bytes.toByteArray())
            } catch (e: IOException) {
                e.printStackTrace()
                fo.close()
            }

            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    .putExtra(Intent.EXTRA_STREAM, MethodChecker.getUri(context, file))
        }

        share.putExtra(Intent.EXTRA_REFERRER, ProductUri)
                .putExtra(Intent.EXTRA_TEXT, shareTxt)

        if (context != null) {
            val resolveInfo = context.packageManager?.queryIntentActivities(share, 0)
                    ?.firstOrNull { it.activityInfo.packageName == packageName }
                    ?.apply {
                        share.`package` = activityInfo.packageName
                    }

            when {
                resolveInfo != null -> context.startActivity(share)
                altUrl != null -> context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(altUrl)))
                else -> Toast.makeText(context, context.getString(R.string.error_apps_not_installed), Toast.LENGTH_SHORT).show()
            }
        }
    }


    /**
     * Untuk data uri (tidak perlu konversi dari bitmap ke uri)
     *
     * @param packageName nama package diambil di TkpdState.PackageName.*
     * @param targetType  jenis apakah gambar/textonly diambil di TkpdState.PackageName.Type_
     * @param altUrl      URL tempat share apabila apps tidak diinstal pada device, disimpan di TkpdState.PackageName.
     * @author EkaCipta
     */
    /**
     * Cek direktori temporari untuk menyimpan gambar ada atau tidak
     */

    private fun checkTempDirectory() {
        val path = Environment.getExternalStorageDirectory().toString() + File.separator + "tkpdtemp" + File.separator
        val f = File(path)
        if (f.exists() && f.isDirectory) {
            Timber.tag("Files").v("Exist")
            val fs = f.listFiles()
            if (fs != null && fs.size > 5)
            // Hapus jika jumlah gambar temporary > 5
                for (file in fs) {
                    file.delete()
                }
        } else {
            Timber.tag("Files").v("Not Exist")
            f.mkdir() // create directory jika direktori tidak ada
        }
    }

    private fun uniqueCode(): String {
        val IDunique = UUID.randomUUID().toString()
        val id = IDunique.replace("-".toRegex(), "")
        return id.substring(0, 16)
    }

    fun ShareIntentImage(context: Activity, title: String, shareTxt: String,
                         ProductUri: String, icon: Bitmap?) {
        val bytes = ByteArrayOutputStream()
        checkTempDirectory()
        val f = File(Environment.getExternalStorageDirectory().toString() + File.separator
                + "tkpdtemp" + File.separator + uniqueCode() + ".jpg")
        if (icon != null)
            try {
                icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                f.createNewFile()
                val fo = FileOutputStream(f)
                fo.write(bytes.toByteArray())
            } catch (e: IOException) {
                e.printStackTrace()
            }

        if (icon != null && f.exists()) {

            val uri = MethodChecker.getUri(context, f)
            val chooserIntent = createIntent(context, title, shareTxt, uri, true)
            context.startActivity(chooserIntent)
        } else {
            val chooserIntent = createIntent(context, title, shareTxt, null, false)
            context.startActivity(chooserIntent)
        }
    }

    private fun createIntent(context: Context, title: String, shareTxt: String, uri: Uri?,
                             fileExists: Boolean): Intent {
        val share = Intent(Intent.ACTION_SEND)
        val shareTitle: String
        if (fileExists) {
            share.type = "image/*"
            share.putExtra(Intent.EXTRA_STREAM, uri)
            share.putExtra(Intent.EXTRA_TEXT, shareTxt)
            shareTitle = "Share Image!"
        } else {
            share.type = "text/plain"
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
            share.putExtra(Intent.EXTRA_SUBJECT, title)
            share.putExtra(Intent.EXTRA_TEXT, shareTxt)
            shareTitle = "Share Link!"
        }


        val targetedShareIntents = ArrayList<Intent>()
        val resInfo = context.packageManager.queryIntentActivities(share, 0)

        var isShop = UserSession(activity).hasShop()

        if (!resInfo.isEmpty()) {
            for (info in resInfo) {
                val targetedShare = Intent(Intent.ACTION_SEND)
                if (fileExists) {
                    targetedShare.type = "image/*"
                    targetedShare.putExtra(Intent.EXTRA_TEXT, shareTxt)
                    targetedShare.putExtra(Intent.EXTRA_STREAM, uri)
                } else {
                    targetedShare.type = "text/plain"
                    targetedShare.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
                    targetedShare.putExtra(Intent.EXTRA_SUBJECT, title)
                    targetedShare.putExtra(Intent.EXTRA_TEXT, shareTxt)
                }

                val appsName = info.activityInfo.packageName
                if (appsName.equals(context.packageName, ignoreCase = true)) {
                    if (isShop) {
                        targetedShare.setPackage(appsName)
                        targetedShareIntents.add(targetedShare)
                    }
                } else {
                    targetedShare.setPackage(appsName)
                    targetedShareIntents.add(targetedShare)
                }
            }
            val chooserIntent = Intent.createChooser(targetedShareIntents.removeAt(0),
                    shareTitle)
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    targetedShareIntents.toTypedArray<Parcelable>())
            return chooserIntent
        }
        val shareContent = Intent(Intent.ACTION_SEND)
        shareContent.putExtra(Intent.EXTRA_TEXT, shareTxt)
        return Intent.createChooser(shareContent,
                shareTitle)
    }
}