package com.tokopedia.device.info

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.tokopedia.device.info.cache.DeviceInfoCache
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.TimeUnit

object DeviceInfo {

    const val ADVERTISINGID = "ADVERTISINGID"
    const val KEY_ADVERTISINGID = "KEY_ADVERTISINGID"
    const val X_86 = "x86"

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
    fun getAdsId(context: Context): String {
        val appContext = context.applicationContext
        val adsIdCache: String = getCacheAdsId(appContext)
        if (adsIdCache.isNotBlank()) {
            return adsIdCache
        } else {
            var adId = ""
            runBlocking(Dispatchers.IO) {
                try {
                    withTimeout(2000) {
                        adId = getlatestAdId(appContext)
                    }
                } catch (ignored:Exception) { }
            }
            return adId
        }
    }

    fun getlatestAdId(context: Context): String {
        return try {
            val appContext = context.applicationContext
            val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(appContext)
            if (adInfo != null) {
                val adID = adInfo.id
                setCacheAdsId(appContext, adID)
            }
            adInfo.id
        } catch (e: Exception) {
            e.printStackTrace()
            ServerLogger.log(Priority.P2, "FINGERPRINT", mapOf("type" to "$e | ${Build.FINGERPRINT} | ${Build.MANUFACTURER} | ${Build.BRAND} | ${Build.DEVICE} | ${Build.PRODUCT} | ${Build.MODEL} | ${Build.TAGS}"))
            ""
        }
    }

    @JvmStatic
    fun getAdsIdSuspend(context: Context, onSuccessGetAdsId: ((adsId:String)->Unit)?) {
        GlobalScope.launch(Dispatchers.IO) {
            val adsIdCache: String = getCacheAdsId(context)
            if (adsIdCache.isNotBlank()) {
                withContext(Dispatchers.Main) {
                    onSuccessGetAdsId?.invoke(adsIdCache)
                }
            } else {
                val adId = getlatestAdId(context)
                withContext(Dispatchers.Main) {
                    onSuccessGetAdsId?.invoke(adId)
                }
            }
        }
    }

    @JvmStatic
    fun getUUID(context: Context): String {
        return DeviceInfoCache(context).getUUID()
    }

    private fun getCacheAdsId(context: Context): String {
        val sp = context.getSharedPreferences(ADVERTISINGID, Context.MODE_PRIVATE)
        return sp.getString(KEY_ADVERTISINGID, "") ?: ""
    }

    private fun setCacheAdsId(context: Context, adsId: String) {
        val sp = context.getSharedPreferences(ADVERTISINGID, Context.MODE_PRIVATE)
        sp.edit().putString(KEY_ADVERTISINGID, adsId).apply()
    }

    @JvmStatic
    fun isx86() =
            try {
                if (Build.VERSION.SDK_INT < 21) {
                    Build.CPU_ABI.contains(X_86) || Build.CPU_ABI2.contains(X_86)
                } else {
                    Build.SUPPORTED_ABIS.any { it.contains(X_86) }
                }
            } catch (e: Exception) {
                false
            }

    @JvmStatic
    fun getImei(context: Context): String? {
        return try {
            val deviceInfoCache = DeviceInfoCache(context.applicationContext)
            val (imeiCache, isCached) = deviceInfoCache.getImei()
            if (isCached) {
                return imeiCache
            } else {
                // target sdk 29 can no longer get imei without READ_PRIVILEGED_PHONE_STATE
                // set to empty
                deviceInfoCache.setImei("")
            }
        } catch (e: Exception) {
            ""
        }
    }

    fun getImeiCache(context: Context): Pair<String, Boolean> {
        val deviceInfoCache = DeviceInfoCache(context.applicationContext)
        return deviceInfoCache.getImei()
    }
}

