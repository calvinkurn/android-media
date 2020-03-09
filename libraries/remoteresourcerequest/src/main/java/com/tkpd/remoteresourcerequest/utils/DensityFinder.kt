package com.tkpd.remoteresourcerequest.utils

import android.content.Context
import android.util.DisplayMetrics
import timber.log.Timber


class DensityFinder {

    companion object {
        fun findDensity(context: Context): String {

            when (context.resources.displayMetrics.densityDpi) {
                DisplayMetrics.DENSITY_LOW -> {
                    Timber.d("ResourceDownloadManager: LDPI")
                    return "ldpi"
                }
                DisplayMetrics.DENSITY_MEDIUM -> {
                    Timber.d("ResourceDownloadManager: MDPI")
                    return "mdpi"

                }
                DisplayMetrics.DENSITY_TV, DisplayMetrics.DENSITY_HIGH -> {
                    Timber.d("ResourceDownloadManager: HDPI")
                    return "hdpi"

                }
                DisplayMetrics.DENSITY_XHIGH, DisplayMetrics.DENSITY_280 -> {
                    Timber.d("ResourceDownloadManager: XHDPI")
                    return "xhdpi"
                }
                DisplayMetrics.DENSITY_XXHIGH, DisplayMetrics.DENSITY_360, DisplayMetrics.DENSITY_400, DisplayMetrics.DENSITY_420 -> {
                    Timber.d("ResourceDownloadManager: XXHDPI")
                    return "xxhdpi"
                }
                DisplayMetrics.DENSITY_XXXHIGH, DisplayMetrics.DENSITY_560 -> {
                    Timber.d("ResourceDownloadManager: XXXHDPI")
                    return "xxxhdpi"
                }
            }
            return ""
        }
    }
}