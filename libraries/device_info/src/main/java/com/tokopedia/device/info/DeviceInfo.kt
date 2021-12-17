package com.tokopedia.device.info

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.tokopedia.device.info.cache.DeviceInfoCache
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume

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
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su"
        )
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
        return if (adsIdCache.isNotBlank()) {
            adsIdCache
        } else {
            runBlocking { getlatestAdId(context, 3000L) }
        }
    }

    @JvmStatic
    fun getUserDeviceName(context: Context): String {
        return try {
            val bluetoothName = Settings.Secure.getString(context.contentResolver, "bluetooth_name")
            if (bluetoothName.isNullOrEmpty()) {
                Settings.Secure.getString(context.contentResolver, "device_name")
            } else {
                bluetoothName
            }
        } catch (e: Exception) {
            ""
        }
    }

    suspend fun getlatestAdId(context: Context, timeOutInMillis: Long = 10000L): String {
        return withContext(Dispatchers.IO) {
            try {
                val appContext = context.applicationContext
                val adId = withTimeout(timeOutInMillis) {
                    val adInfo: AdvertisingIdClient.Info? =
                        suspendCancellableCoroutine { continuation ->
                            try {
                                val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(appContext)
                                continuation.resume(adInfo)
                            } catch (e: Exception) {
                                continuation.resume(null)
                            }
                        }
                    val adIdTemp = if (adInfo == null) {
                        ""
                    } else {
                        val trimAdId = trimGoogleAdId(adInfo.id)
                        setCacheAdsId(appContext, trimAdId)
                        trimAdId
                    }
                    adIdTemp
                }
                adId
            } catch (e: Exception) {
                ServerLogger.log(
                    Priority.P2,
                    "FINGERPRINT",
                    mapOf("type" to "$e | ${Build.FINGERPRINT} | ${Build.MANUFACTURER} | ${Build.BRAND} | ${Build.DEVICE} | ${Build.PRODUCT} | ${Build.MODEL} | ${Build.TAGS}")
                )
                ""
            }
        }
    }

    private fun trimGoogleAdId(googleAdsId: String): String {
        val sb =
            StringBuilder(googleAdsId.length) //we know this is the capacity so we initialise with it:
        for (element in googleAdsId) {
            when (element) {
                '\u2013', '\u2014', '\u2015' -> sb.append('-')
                else -> sb.append(element)
            }
        }
        return sb.toString()
    }

    // Initialize ads Id in background
    @JvmOverloads
    @JvmStatic
    fun getAdsIdSuspend(
        context: Context,
        onSuccessGetAdsId: ((adsId: String) -> Unit)? = null,
        timeOutInMillis: Long = 3000L
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            val adsIdCache: String = getCacheAdsId(context)
            if (adsIdCache.isNotBlank()) {
                withContext(Dispatchers.Main) {
                    onSuccessGetAdsId?.invoke(adsIdCache)
                }
            } else {
                val adId = getlatestAdId(context, timeOutInMillis)
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
            Build.SUPPORTED_ABIS.any { it.contains(X_86) }
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

