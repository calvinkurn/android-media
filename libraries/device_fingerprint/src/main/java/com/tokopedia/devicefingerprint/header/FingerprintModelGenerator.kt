package com.tokopedia.devicefingerprint.header

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import com.tokopedia.config.GlobalConfig
import com.tokopedia.device.info.DeviceConnectionInfo.getCarrierName
import com.tokopedia.device.info.DeviceConnectionInfo.getHttpAgent
import com.tokopedia.device.info.DeviceConnectionInfo.getSSID
import com.tokopedia.device.info.DeviceInfo
import com.tokopedia.device.info.DeviceInfo.getAndroidId
import com.tokopedia.device.info.DeviceInfo.getImei
import com.tokopedia.device.info.DeviceInfo.getLanguage
import com.tokopedia.device.info.DeviceInfo.getManufacturerName
import com.tokopedia.device.info.DeviceInfo.getModelName
import com.tokopedia.device.info.DeviceInfo.getOSName
import com.tokopedia.device.info.DeviceInfo.getPackageName
import com.tokopedia.device.info.DeviceInfo.getTimeZoneOffset
import com.tokopedia.device.info.DeviceInfo.getUUID
import com.tokopedia.device.info.DeviceInfo.isEmulated
import com.tokopedia.device.info.DeviceInfo.isRooted
import com.tokopedia.device.info.DeviceInfo.isx86
import com.tokopedia.device.info.DevicePerformanceInfo.getAvailableProcessor
import com.tokopedia.device.info.DevicePerformanceInfo.getDeviceDpi
import com.tokopedia.device.info.DevicePerformanceInfo.getDeviceMemoryClassCapacity
import com.tokopedia.device.info.DeviceScreenInfo.getScreenResolution
import com.tokopedia.device.info.DeviceScreenInfo.isTablet
import com.tokopedia.devicefingerprint.datavisor.instance.VisorFingerprintInstance
import com.tokopedia.devicefingerprint.header.model.FingerPrint
import com.tokopedia.devicefingerprint.location.LocationCache
import com.tokopedia.encryption.security.toBase64
import com.tokopedia.network.data.model.FingerprintModel
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import java.util.*

object FingerprintModelGenerator {
    private const val FINGERPRINT_KEY_NAME = "FINGERPRINT_KEY_NAME"
    private const val FINGERPRINT_USE_CASE = "FINGERPRINT_USE_CASE"
    private const val FINGERPRINT_TS = "timestamp"
    private const val FINGERPRINT_EXPIRED_TIME = 3_600
    private const val AT_TOKOPEDIA = "@tokopedia"

    private var userSession: UserSessionInterface? = null
    private var fingerprintLastTs = -1L
    private var fingerprintCache = ""

    @JvmStatic
    fun generateFingerprintModel(context: Context): FingerprintModel {
        val fingerprintModel = FingerprintModel()
        val fingerprintString = getFingerPrintJson(context).toBase64()
        fingerprintModel.adsId = DeviceInfo.getAdsId(context)
        fingerprintModel.fingerprintHash = fingerprintString
        fingerprintModel.registrarionId = getFCMId(context)
        return fingerprintModel
    }

    fun getFCMId(context: Context): String {
        val userSession = UserSession(context)
        val deviceId = userSession.deviceId
        if (deviceId.isNullOrEmpty()) {
            val uuid = getUUID(context)
            userSession.deviceId = uuid
            return uuid
        } else {
            return deviceId
        }
    }

    private fun getFingerPrintJson(context: Context): String {
        if (fingerprintCache.isEmpty()) {
            fingerprintCache =
                getFingerprintSharedPref(context).getString(FINGERPRINT_USE_CASE, "") ?: ""
        }
        val now = (System.currentTimeMillis() / 1000)
        if (fingerprintCache.isEmpty() || isFingerprintExpired(context, now)) {
            fingerprintCache = generateFingerprintData(context)
            getFingerprintSharedPref(context).edit().putString(FINGERPRINT_USE_CASE, fingerprintCache)
                .putLong(FINGERPRINT_TS, now).apply()
            fingerprintLastTs = now
        }
        return fingerprintCache
    }

    private fun getFingerprintSharedPref(context: Context): SharedPreferences {
        return context.getSharedPreferences(FINGERPRINT_KEY_NAME, Context.MODE_PRIVATE)
    }

    private fun isFingerprintExpired(context: Context, now: Long): Boolean {
        if (fingerprintLastTs == -1L) {
            fingerprintLastTs = getFingerprintSharedPref(context).getLong(FINGERPRINT_TS, 0)
        }
        return now - fingerprintLastTs > FINGERPRINT_EXPIRED_TIME
    }

    private fun generateFingerprintData(context: Context): String {
        val deviceName = getModelName()
        val deviceFabrik = getManufacturerName()
        val deviceOS = getOSName()
        val deviceSystem = "android"
        val isRooted = isRooted()
        val timezone = getTimeZoneOffset()
        val userAgent = getHttpAgent()
        val isEmulator = isEmulated()
        val isTablet = isTablet(context)
        val screenReso = getScreenResolution(context)
        val deviceLanguage = getLanguage()
        val ssid = getSSID(context)
        val carrier = getCarrierName(context)
        val androidId = getAndroidId(context)
        val imei = getImei(context)
        val isx86 = isx86()
        val packageName = getPackageName(context)
        val uuid = getUUID(context)
        val isNakama = isNakama(getUserSession(context))
        val deviceAvailableProcessor = getAvailableProcessor(context.applicationContext)
        val deviceMemoryClass = getDeviceMemoryClassCapacity(context.applicationContext)
        val deviceDpi = getDeviceDpi(context.applicationContext)
        val fp = FingerPrint(
                unique_id = DeviceInfo.getAdsId(context),
                device_name = deviceName,
                device_manufacturer = deviceFabrik,
                device_model = deviceName,
                device_system = deviceSystem,
                current_os = deviceOS,
                is_jailbroken_rooted = isRooted,
                timezone = timezone,
                user_agent = userAgent,
                is_emulator = isEmulator,
                is_tablet = isTablet,
                screen_resolution = screenReso,
                language = deviceLanguage,
                ssid = ssid,
                carrier = carrier,
                location_latitude = LocationCache.getLatitudeCache(context),
                location_longitude = LocationCache.getLongitudeCache(context),
                androidId = androidId,
                isx86 = isx86,
                packageName = packageName,
                is_nakama = isNakama.toString().toUpperCase(Locale.ROOT),
                availableProcessor = deviceAvailableProcessor,
                deviceMemoryClassCapacity = deviceMemoryClass,
                deviceDpi = deviceDpi,
                pid = imei,
                uuid = uuid,
                inval = VisorFingerprintInstance.getDVToken(context),
                installer = context.packageManager.getInstallerPackageName(context.packageName)?: "")
        return Gson().toJson(fp)
    }

    fun getUserSession(context: Context): UserSessionInterface {
        if (userSession == null) {
            userSession = UserSession(context)
        }
        return userSession!!
    }

    private fun isNakama(userSession: UserSessionInterface): Boolean {
        return (GlobalConfig.DEBUG || userSession.email?.contains(AT_TOKOPEDIA) ?: false)
    }
}
