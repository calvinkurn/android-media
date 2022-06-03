package com.tokopedia.imagepreview.imagesecure

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.imagepreview.R
import com.tokopedia.media.loader.loadSecureImageWithEmptyTarget
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.file.PublicFolderUtil
import java.io.File
import java.util.ArrayList

class ImageSecurePreviewActivity : ImagePreviewActivity() {

    private var touchImageAdapter: TouchImageAdapterKt? = null
    private var isSecure: Boolean = false
    private var userSession: UserSessionInterface? = null

    override fun setupAdapter() {
        isSecure = intent.extras?.getBoolean(IS_SECURE, false) ?: false
        userSession = UserSession(this@ImageSecurePreviewActivity).apply {
            touchImageAdapter = TouchImageAdapterKt(fileLocations, this, isSecure)
            touchImageAdapter?.setOnImageStateChangeListener(
                object : TouchImageAdapterKt.OnImageStateChange {
                    override fun onStateDefault() {
                        viewPager.SetAllowPageSwitching(true)
                    }

                    override fun onStateZoom() {
                        viewPager.SetAllowPageSwitching(false)
                    }
                }
            )
            viewPager.adapter = touchImageAdapter
            viewPager.currentItem = position
        }
    }

    override fun actionDownloadAndSavePicture() {
        val filenameParam = FileUtil.generateUniqueFileNameDateFormat(viewPager.currentItem)
        val notificationId = filenameParam.hashCode()
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(this, ANDROID_GENERAL_CHANNEL)
        notifyProcessDownload(
            filenameParam,
            notificationId,
            notificationBuilder,
            notificationManager
        )
        downloadImage(filenameParam, notificationId, notificationBuilder, notificationManager)
    }

    private fun notifyProcessDownload(
        filenameParam: String,
        notificationId: Int,
        notificationBuilder: NotificationCompat.Builder,
        notificationManager: NotificationManager
    ) {
        notificationBuilder.setContentTitle(filenameParam)
            .setContentText(getString(R.string.download_in_process))
            .setSmallIcon(com.tokopedia.design.R.drawable.ic_stat_notify_white)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    com.tokopedia.design.R.drawable.ic_big_notif_customerapp
                )
            )
            .setAutoCancel(true)
        notificationBuilder.setProgress(0, 0, true);
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun downloadImage(
        filenameParam: String,
        notificationId: Int,
        notificationBuilder: NotificationCompat.Builder,
        notificationManager: NotificationManager
    ) {
        val mediaTarget = MediaBitmapEmptyTarget<Bitmap>(
            onReady = {
                onResourceImageReady(
                    filenameParam, it, notificationId, notificationBuilder, notificationManager
                )
            },
            onFailed = {
                showFailedDownload(notificationId, notificationBuilder)
            }
        )
        fileLocations?.getOrNull(viewPager.currentItem)?.let { url ->
            userSession?.let {
                loadSecureImageWithEmptyTarget(
                    context = this@ImageSecurePreviewActivity,
                    url = url,
                    userSession = it,
                    mediaTarget = mediaTarget
                )
            }
        }
    }

    private fun onResourceImageReady(
        filenameParam: String,
        resource: Bitmap,
        notificationId: Int,
        notificationBuilder: NotificationCompat.Builder,
        notificationManager: NotificationManager
    ) {
        val fileAndUri: Pair<File?, Uri?>
        try {
            fileAndUri = PublicFolderUtil.putImageToPublicFolder(
                this@ImageSecurePreviewActivity,
                bitmap = resource,
                fileName = filenameParam
            )
        } catch (e: Throwable) {
            showFailedDownload(notificationId, notificationBuilder)
            return
        }
        val resultUri = fileAndUri.second
        if (resultUri == null) {
            showFailedDownload(notificationId, notificationBuilder)
        } else {
            notifyDownloadImageNotification(
                resultUri, notificationId, notificationBuilder, notificationManager
            )
            showSnackBar(resultUri)
        }
    }

    private fun notifyDownloadImageNotification(
        resultUri: Uri,
        notificationId: Int,
        notificationBuilder: NotificationCompat.Builder,
        notificationManager: NotificationManager
    ) {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
        }
        intent.setDataAndType(resultUri, "image/*")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val pIntent = PendingIntent.getActivity(
            this@ImageSecurePreviewActivity, 0, intent, 0
        )

        notificationBuilder.setContentText(getString(R.string.download_success))
            .setProgress(0, 0, false)
            .setContentIntent(pIntent);

        notificationBuilder.build().flags =
            notificationBuilder.build().flags or Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(notificationId, notificationBuilder.build());
        this@ImageSecurePreviewActivity.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun showSnackBar(resultUri: Uri) {
        val snackBar = SnackbarManager.make(
            findViewById<View>(android.R.id.content),
            getString(R.string.download_success),
            Snackbar.LENGTH_SHORT
        )
        snackBar.setAction(getString(R.string.label_open)) {
            openImageDownloaded(resultUri)
        }
        snackBar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                this@ImageSecurePreviewActivity.window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
        })
        snackBar.show()
    }

    companion object {
        private const val IS_SECURE = "is_image_secure"

        @JvmStatic
        @JvmOverloads
        fun getCallingIntent(
            context: Context,
            imageUris: ArrayList<String>,
            imageDesc: ArrayList<String>? = null,
            position: Int = 0,
            title: String? = null,
            description: String? = null,
            disableDownloadButton: Boolean = true,
            isSecure: Boolean = false
        ): Intent {
            val intent = Intent(context, ImageSecurePreviewActivity::class.java)
            val bundle = Bundle()
            bundle.putString(TITLE, title)
            bundle.putString(DESCRIPTION, description)
            bundle.putStringArrayList(IMAGE_URIS, imageUris)
            bundle.putStringArrayList(IMAGE_DESC, imageDesc)
            bundle.putInt(IMG_POSITION, position)
            bundle.putBoolean(DISABLE_DOWNLOAD, disableDownloadButton)
            bundle.putBoolean(IS_SECURE, isSecure)
            intent.putExtras(bundle)
            return intent
        }
    }
}