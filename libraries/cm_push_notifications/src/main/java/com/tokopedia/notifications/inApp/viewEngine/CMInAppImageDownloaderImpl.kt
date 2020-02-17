package com.tokopedia.notifications.inApp.viewEngine

import android.content.Context
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp

class CMInAppImageDownloaderImpl(cmInApp: CMInApp) : CMInAppImageDownloader(cmInApp) {
    override suspend fun verifyAndUpdate() {
        cmInApp.cmLayout?.run {
            if (null != img && (img.startsWith("http") || img.startsWith("www")))
                cmInApp.type = CmInAppConstant.TYPE_DROP
        }

    }

    override suspend fun downloadImages(context: Context): CMInApp {
        val imageSizeAndTimeout: ImageSizeAndTimeout = when(cmInApp.type) {
            CmInAppConstant.TYPE_FULL_SCREEN_IMAGE_ONLY,  CmInAppConstant.TYPE_INTERSTITIAL_IMAGE_ONLY -> ImageSizeAndTimeout.getBigImageSize(context)
            CmInAppConstant.TYPE_INTERSTITIAL, CmInAppConstant.TYPE_FULL_SCREEN -> ImageSizeAndTimeout.getCenterImageSize(context)
            else -> ImageSizeAndTimeout.getAlertImageSize(context)
        }
        cmInApp.cmLayout?.run {
            val filePath = downloadAndStore(context, img, imageSizeAndTimeout)
            filePath?.let {
                img = filePath
            }
        }

        verifyAndUpdate()
        return cmInApp
    }

}
