package com.tokopedia.test.application.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.TimeUnit

object DeviceInfo {

    @JvmStatic
    fun isRooted(): Boolean {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3()
    }

    @JvmStatic
    fun getModelName(): String {
        return Build.MODEL
    }

    @JvmStatic
    fun getManufacturerName(): String {
        return Build.MANUFACTURER
    }

    @JvmStatic
    fun getOSName(): String {
        return Build.VERSION.RELEASE
    }

    @JvmStatic
    fun getLanguage(): String {
        return Locale.getDefault().toString()
    }

    @JvmStatic
    fun isEmulated(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || "google_sdk" == Build.PRODUCT)
    }

    @JvmStatic
    fun getTimeZoneOffset(): String {
        val mCalendar = GregorianCalendar()
        val mTimeZone = mCalendar.timeZone
        val mGMTOffset = mTimeZone.rawOffset
        val offset = TimeUnit.HOURS.convert(mGMTOffset.toLong(), TimeUnit.MILLISECONDS)
        return if (offset < 0)
            "GMT" + TimeUnit.HOURS.convert(mGMTOffset.toLong(), TimeUnit.MILLISECONDS)
        else
            "GMT+" + TimeUnit.HOURS.convert(mGMTOffset.toLong(), TimeUnit.MILLISECONDS)
    }

    private fun checkRootMethod1(): Boolean {
        val buildTags = Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkRootMethod2(): Boolean {
        val paths = arrayOf("/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su")
        for (path in paths) {
            if (File(path).exists()) return true
        }
        return false
    }

    private fun checkRootMethod3(): Boolean {
        var process: Process? = null
        return try {
            process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val `in` = BufferedReader(InputStreamReader(process!!.inputStream))
            `in`.readLine() != null
        } catch (t: Throwable) {
            false
        } finally {
            process?.destroy()
        }
    }

    @JvmStatic
    fun getPackageName(context: Context): String {
        return try {
            context.packageName
        } catch (e: Exception) {
            ""
        }
    }

    @JvmStatic
    @SuppressLint("HardwareIds")
    fun getAndroidId(context: Context): String {
        return try {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        } catch (e: Exception) {
            ""
        }
    }

    @JvmStatic
    fun isx86() =
        try {
            if (Build.VERSION.SDK_INT < 21) {
                Build.CPU_ABI.contains("x86") ||  Build.CPU_ABI.contains("x86")
            } else {
                Build.SUPPORTED_ABIS.any { it.contains("x86") }
            }
        } catch (e: Exception) {
            false
        }

    @JvmStatic
    fun getInstalledAppsCount(context: Context): Int {
        return try {
            context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA).size
        } catch (e: Exception) {
            0
        }
    }

}

