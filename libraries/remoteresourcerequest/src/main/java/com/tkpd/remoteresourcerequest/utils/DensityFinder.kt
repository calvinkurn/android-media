package com.tkpd.remoteresourcerequest.utils

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics


class DensityFinder {
    companion object {
        private const val MDPI = "mdpi"
        private const val HDPI = "hdpi"
        private const val XHDPI = "xhdpi"
        private const val XXHDPI = "xxhdpi"
        private const val XXXHDPI = "xxxhdpi"

        var densityUrlPath = ""
        var decidedImageDensity = DisplayMetrics.DENSITY_MEDIUM


        fun initializeDensityPath(context: Context) {
            try {
                when (context.resources.displayMetrics.densityDpi) {
                    DisplayMetrics.DENSITY_LOW,
                    DisplayMetrics.DENSITY_MEDIUM -> {
                        decidedImageDensity = DisplayMetrics.DENSITY_MEDIUM
                        densityUrlPath = MDPI
                    }
                    DisplayMetrics.DENSITY_TV, DisplayMetrics.DENSITY_HIGH,
                    DisplayMetrics.DENSITY_180, DisplayMetrics.DENSITY_200,
                    DisplayMetrics.DENSITY_220 -> {
                        decidedImageDensity = DisplayMetrics.DENSITY_HIGH
                        densityUrlPath = HDPI
                    }
                    DisplayMetrics.DENSITY_XHIGH, DisplayMetrics.DENSITY_260,
                    DisplayMetrics.DENSITY_280, DisplayMetrics.DENSITY_300 -> {
                        decidedImageDensity = DisplayMetrics.DENSITY_XHIGH
                        densityUrlPath = XHDPI
                    }
                    DisplayMetrics.DENSITY_XXHIGH, DisplayMetrics.DENSITY_340,
                    DisplayMetrics.DENSITY_360, DisplayMetrics.DENSITY_400,
                    DisplayMetrics.DENSITY_420, DisplayMetrics.DENSITY_440 -> {
                        decidedImageDensity = DisplayMetrics.DENSITY_XXHIGH
                        densityUrlPath = XXHDPI
                    }
                    DisplayMetrics.DENSITY_XXXHIGH, DisplayMetrics.DENSITY_560 -> {

                        /***** Added in android 18 so prior versions will at max shows XXHDPI ****/
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            decidedImageDensity = DisplayMetrics.DENSITY_XXXHIGH
                        }
                        densityUrlPath = XXXHDPI
                    }
                    else -> {
                        decidedImageDensity = DisplayMetrics.DENSITY_MEDIUM
                        densityUrlPath = MDPI
                    }
                }
            } catch (e: Exception) {
                decidedImageDensity = DisplayMetrics.DENSITY_MEDIUM
                densityUrlPath = MDPI
            }
        }
    }
}
