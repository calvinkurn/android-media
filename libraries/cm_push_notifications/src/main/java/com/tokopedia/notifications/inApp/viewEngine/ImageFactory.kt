package com.tokopedia.notifications.inApp.viewEngine

import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp

class ImageFactory(val cmInApp: CMInApp) {

    private fun getNotificationImageDownloader(): CMInAppImageDownloader? {
        if (cmInApp.endTime < System.currentTimeMillis())
            return null

        return CMInAppImageDownloaderImpl(cmInApp)
    }

    companion object {
        fun provideNotificationImageDownloader(baseNotificationModel: CMInApp): CMInAppImageDownloader? {
            return ImageFactory(baseNotificationModel).getNotificationImageDownloader()
        }
    }
}

