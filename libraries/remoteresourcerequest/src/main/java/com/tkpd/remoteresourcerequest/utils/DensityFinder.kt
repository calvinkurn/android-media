package com.tkpd.remoteresourcerequest.utils

import android.content.Context
import android.util.DisplayMetrics
import java.lang.Exception


class DensityFinder {

    companion object {
        fun findDensity(context: Context, callback: DeferredCallback?): String {

            try {
                when (context.resources.displayMetrics.densityDpi) {
                    DisplayMetrics.DENSITY_LOW -> {
                        CallbackDispatcher.dispatchLog(
                                callback,
                                "device density = LDPI")
                        return "ldpi"
                    }
                    DisplayMetrics.DENSITY_MEDIUM -> {
                        CallbackDispatcher.dispatchLog(
                                callback,
                                "device density = MDPI")
                        return "mdpi"
                    }
                    DisplayMetrics.DENSITY_TV, DisplayMetrics.DENSITY_HIGH,
                    DisplayMetrics.DENSITY_180, DisplayMetrics.DENSITY_200,
                    DisplayMetrics.DENSITY_220 -> {
                        CallbackDispatcher.dispatchLog(
                                callback,
                                "device density = HDPI")
                        return "hdpi"
                    }
                    DisplayMetrics.DENSITY_XHIGH, DisplayMetrics.DENSITY_260,
                    DisplayMetrics.DENSITY_280, DisplayMetrics.DENSITY_300 -> {
                        CallbackDispatcher.dispatchLog(
                                callback,
                                "device density = XHDPI")
                        return "xhdpi"
                    }
                    DisplayMetrics.DENSITY_XXHIGH, DisplayMetrics.DENSITY_340,
                    DisplayMetrics.DENSITY_360, DisplayMetrics.DENSITY_400,
                    DisplayMetrics.DENSITY_420, DisplayMetrics.DENSITY_440 -> {
                        CallbackDispatcher.dispatchLog(
                                callback,
                                "device density = XXHDPI"
                        )
                        return "xxhdpi"
                    }
                    DisplayMetrics.DENSITY_XXXHIGH, DisplayMetrics.DENSITY_560 -> {
                        CallbackDispatcher.dispatchLog(
                                callback,
                                "device density = XXXHDPI"
                        )
                        return "xxxhdpi"
                    }
                }

                return "mdpi"
            } catch (e: Exception) {
                return "mdpi"
            }
        }
    }
}
