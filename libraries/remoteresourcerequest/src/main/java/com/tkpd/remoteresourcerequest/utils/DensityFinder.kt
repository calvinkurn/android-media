package com.tkpd.remoteresourcerequest.utils

import android.content.Context
import android.util.DisplayMetrics
import android.util.Log


class DensityFinder {

    companion object {
        fun findDensity(context: Context): String {

            when (context.resources.displayMetrics.densityDpi) {
                DisplayMetrics.DENSITY_LOW -> {
                    Log.d("DensityFinder", "LDPI")
                    return "ldpi"
                }
                DisplayMetrics.DENSITY_MEDIUM -> {
                    Log.d("DensityFinder", "MDPI")
                    return "mdpi"

                }
                DisplayMetrics.DENSITY_TV, DisplayMetrics.DENSITY_HIGH -> {
                    Log.d("DensityFinder", "HDPI")
                    return "hdpi"

                }
                DisplayMetrics.DENSITY_XHIGH, DisplayMetrics.DENSITY_280 -> {
                    Log.d("DensityFinder", "XHDPI")
                    return "xhdpi"
                }
                DisplayMetrics.DENSITY_XXHIGH, DisplayMetrics.DENSITY_360, DisplayMetrics.DENSITY_400, DisplayMetrics.DENSITY_420 -> {
                    Log.d("DensityFinder", "XXHDPI")
                    return "xxhdpi"
                }
                DisplayMetrics.DENSITY_XXXHIGH, DisplayMetrics.DENSITY_560 -> {
                    Log.d("DensityFinder", "XXXHDPI")
                    return "xxxhdpi"
                }
            }
            return ""
        }
    }
}