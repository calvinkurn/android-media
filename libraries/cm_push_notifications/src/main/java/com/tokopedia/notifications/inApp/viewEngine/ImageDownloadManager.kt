package com.tokopedia.notifications.inApp.viewEngine

import android.content.Context
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp

class ImageDownloadManager(val context: Context, val cmInApp: CMInApp) {

    suspend fun startImageDownload(): CMInApp {
        return ImageFactory.provideNotificationImageDownloader(cmInApp)
                ?.downloadImages(context)
                ?: cmInApp
    }

    companion object {
        suspend fun downloadImages(context: Context, cmInApp: CMInApp): CMInApp {
            return ImageDownloadManager(context, cmInApp).startImageDownload()
        }
    }
}