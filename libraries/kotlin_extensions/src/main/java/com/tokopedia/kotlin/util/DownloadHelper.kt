package com.tokopedia.kotlin.util

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import org.jetbrains.annotations.NotNull


/**
 * @param context  to be used to fetch download service
 * @param uri you need to pass the URI of the file to be downloaded.
 * @param filename to be shown in notification and name of the file to be saved
 * @param listener (optional) to update download complete
 */

class DownloadHelper(@NotNull val context: Context,
                     @NotNull val uri: String,
                     @NotNull val filename: String,
                     var listener: DownloadHelperListener?) {

    fun downloadFile(isDownloadable: (String) -> Boolean) {
        val downloadUri = Uri.parse(uri)

        if (!isDownloadable(uri)) return

        val downloadManager: DownloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            setAllowedOverRoaming(true)
            setTitle(filename)
            setDescription(filename)
            setVisibleInDownloadsUi(true)
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
        }
        context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        downloadManager.enqueue(request)
    }


    private var onComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context, intent: Intent) {
            listener?.apply {
                onDownloadComplete()
            }

            unSubcribeDownLoadHelper()
            listener = null
        }
    }

    private fun unSubcribeDownLoadHelper() {
        context.apply {
            unregisterReceiver(onComplete)
        }

    }


    interface DownloadHelperListener {
        fun onDownloadComplete()
    }

}