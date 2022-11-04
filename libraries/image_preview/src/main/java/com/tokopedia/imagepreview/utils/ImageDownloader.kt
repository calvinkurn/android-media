package com.tokopedia.imagepreview.utils

import android.app.Activity
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.imagepreview.R
import com.tokopedia.imagepreview.notification.DownloadImageNotification
import com.tokopedia.media.loader.loadSecureImageWithEmptyTarget
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.file.PublicFolderUtil
import java.io.File

class ImageDownloader(
    private val context: Context,
    private val fileLocations: List<String>?,
    private val userSessionInterface: UserSessionInterface
) {

    private var downloadImageNotification: DownloadImageNotification? = null

    fun downloadImage(
        viewPagePosition: Int,
        filenameParam: String,
        notificationId: Int = -1,
        notificationBuilder: NotificationCompat.Builder? = null,
        notificationManager: NotificationManager? = null,
        onActionClick: (Uri) -> Unit,
        onDismissed: () -> Unit
    ) {
        if (notificationId != -1 &&
            notificationBuilder != null &&
            notificationManager != null
        ) {
            downloadImageNotification = DownloadImageNotification(
                notificationId, notificationBuilder, notificationManager
            )
            downloadImageNotification?.notifyProcessDownload(
                filenameParam,
                context.getString(R.string.download_in_process),
                getLargeTokopediaIcon()
            )
        }
        val downloadFailText = context.getString(R.string.download_failed)
        val mediaTarget = getMediaBitmapEmptyTarget(
            filenameParam, downloadFailText, onActionClick, onDismissed
        )
        fileLocations?.getOrNull(viewPagePosition)?.let { url ->
            loadSecureImageWithEmptyTarget(
                context = context,
                url = url,
                userSession = userSessionInterface,
                mediaTarget = mediaTarget
            )
        }
    }

    private fun getMediaBitmapEmptyTarget(
        filenameParam: String,
        downloadFailText: String,
        onActionClick: (Uri) -> Unit,
        onDismissed: () -> Unit
    ): MediaBitmapEmptyTarget<Bitmap> {
        return MediaBitmapEmptyTarget (
            onReady = {
                onResourceImageReady(
                    filenameParam, it, downloadFailText, onActionClick, onDismissed
                )
            },
            onFailed = {
                downloadImageNotification?.notifyNotification(contentText = downloadFailText)
                showToasterError(downloadFailText)
            }
        )
    }

    private fun onResourceImageReady(
        filenameParam: String,
        resource: Bitmap,
        downloadFailText: String,
        onActionClick: (Uri) -> Unit,
        onDismissed: () -> Unit
    ) {
        val fileAndUri: Pair<File?, Uri?>
        try {
            fileAndUri = PublicFolderUtil.putImageToPublicFolder(
                context = context,
                bitmap = resource,
                fileName = filenameParam
            )
        } catch (e: Throwable) {
            downloadImageNotification?.notifyNotification(contentText = downloadFailText)
            showToasterError(downloadFailText)
            return
        }
        val resultUri = fileAndUri.second
        if (resultUri == null) {
            downloadImageNotification?.notifyNotification(contentText = downloadFailText)
            showToasterError(downloadFailText)
        } else {
            notifyDownloadSuccess(resultUri, onActionClick, onDismissed)
        }
    }

    private fun notifyDownloadSuccess(
        resultUri: Uri,
        onActionClick: (Uri) -> Unit,
        onDismissed: () -> Unit
    ) {
        val downloadSuccessText = context.getString(R.string.download_success)
        val pendingIntent = createPendingIntent(resultUri)
        downloadImageNotification?.notifyNotification(
            pendingIntent = pendingIntent,
            contentText = downloadSuccessText
        )
        showSnackBar(
            resultUri = resultUri,
            contentText = downloadSuccessText,
            actionText = context.getString(R.string.image_preview_label_open),
            onActionClick = onActionClick,
            onDismissed = onDismissed
        )
    }

    private fun createPendingIntent(resultUri: Uri): PendingIntent {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
        }
        intent.setDataAndType(resultUri, "image/*")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    private fun showSnackBar(
        resultUri: Uri,
        contentText: String,
        actionText: String,
        onActionClick: (Uri) -> Unit,
        onDismissed: () -> Unit
    ) {
        try {
            val view = (context as Activity).findViewById<View>(android.R.id.content)
            val snackBar = SnackbarManager.make(view, contentText, Snackbar.LENGTH_SHORT)
            snackBar.setAction(actionText) {
                onActionClick.invoke(resultUri)
            }
            snackBar.addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    onDismissed.invoke()
                }
            })
            snackBar.show()
        } catch (ignored: Throwable) {}
    }

    private fun showToasterError(contentText: String) {
        try {
            val view = (context as Activity).findViewById<View>(android.R.id.content)
            Toaster.build(
                view, contentText,
                Toast.LENGTH_SHORT, Toaster.TYPE_ERROR, OK_TEXT
            ).show()
        } catch (ignored: Throwable) {}
    }

    private fun getLargeTokopediaIcon(): Bitmap {
        return BitmapFactory.decodeResource(
            context.resources,
            com.tokopedia.design.R.drawable.ic_big_notif_customerapp
        )
    }

    companion object {
        private const val OK_TEXT = "Ok"
    }
}
