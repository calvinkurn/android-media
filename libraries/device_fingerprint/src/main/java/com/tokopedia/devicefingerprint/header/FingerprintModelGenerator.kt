package com.tokopedia.devicefingerprint.header

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.bytedance.common.utility.NetworkUtils
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.tokopedia.cachemanager.PersistentCacheManager
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
import com.tokopedia.device.info.model.AdditionalDeviceInfo
import com.tokopedia.devicefingerprint.datavisor.instance.VisorFingerprintInstance
import com.tokopedia.devicefingerprint.header.model.FingerPrint
import com.tokopedia.devicefingerprint.header.model.FingerPrintNew
import com.tokopedia.devicefingerprint.location.LocationCache.DEFAULT_LATITUDE
import com.tokopedia.devicefingerprint.location.LocationCache.DEFAULT_LONGITUDE
import com.tokopedia.encryption.security.toBase64
import com.tokopedia.locationmanager.DeviceLocation
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.locationmanager.LocationDetectorHelper.Companion.LOCATION_CACHE
import com.tokopedia.network.data.model.FingerprintModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.remoteconfig.RemoteConfigKey.ANDROID_ENABLE_NEW_FINGERPRINT_HEADER_DATA
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.coroutines.CoroutineContext

object FingerprintModelGenerator : CoroutineScope {
    private const val FINGERPRINT_KEY_NAME = "FINGERPRINT_KEY_NAME"
    private const val FINGERPRINT_USE_CASE = "FINGERPRINT_USE_CASE"
    private const val FINGERPRINT_TS = "timestamp"
    private const val FINGERPRINT_EXPIRED_TIME = 3_600
    private const val AT_TOKOPEDIA = "@tokopedia"

    private var userSession: UserSessionInterface? = null
    private var fingerprintLastTs = -1L
    private var fingerprintHasAdsId = false
    private var fingerprintCache = ""

    @SuppressLint("StaticFieldLeak")
    private var locationDetectorHelper: LocationDetectorHelper? = null

    private var locationFlow: Flow<DeviceLocation?>? = null
    var locationFlowJob: Job? = null

    @JvmStatic
    fun generateFingerprintModel(context: Context): FingerprintModel {
        val fingerprintModel = FingerprintModel()
        // adsid need to be gotten first, because there is possibility it make fingerprintJson expire.
        fingerprintModel.adsId = DeviceInfo.getAdsId(context)
        fingerprintModel.fingerprintHash = getFingerPrintJson(context).toBase64()
        fingerprintModel.registrarionId = getFCMId(context)
        return fingerprintModel
    }

    fun getFCMId(context: Context): String {
        val userSession = UserSession(context)
        val deviceId = userSession.deviceId
        return if (deviceId.isNullOrEmpty()) {
            val uuid = getUUID(context)
            userSession.deviceId = uuid
            uuid
        } else {
            deviceId
        }
    }

    private fun getFingerPrintJson(context: Context): String {
        if (fingerprintCache.isEmpty()) {
            fingerprintCache =
                getFingerprintSharedPref(context).getString(FINGERPRINT_USE_CASE, "") ?: ""
        }
        val now = (System.currentTimeMillis() / 1000)
        if (fingerprintCache.isEmpty() || isFingerprintExpired(context, now) ||
            adsIdAlreadyRetrieved(context)
        ) {
            setFingerprintData(context)
            getFingerprintSharedPref(context).edit()
                .putString(FINGERPRINT_USE_CASE, fingerprintCache)
                .putLong(FINGERPRINT_TS, now).apply()
            fingerprintLastTs = now
        }
        return fingerprintCache
    }

    private fun setFingerprintData(context: Context) {
        if (FirebaseRemoteConfigImpl(context).getBoolean(
                ANDROID_ENABLE_NEW_FINGERPRINT_HEADER_DATA,
                true
            )
        ) {
            val fp = generateNewFingerprintData(context)
            fingerprintHasAdsId = fp.hasUniqueId()
            fingerprintCache = Gson().toJson(fp)
        } else {
            val fp = generateFingerprintData(context)
            fingerprintHasAdsId = fp.hasUniqueId()
            fingerprintCache = Gson().toJson(fp)
        }
    }

    private fun adsIdAlreadyRetrieved(context: Context): Boolean {
        return !fingerprintHasAdsId && DeviceInfo.getCacheAdsId(context).isNotEmpty()
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

    fun expireFingerprint() {
        // put time stamp 2 seconds just before it is expired.
        // This is to prevent bursting expire fingerprint at the same time
        fingerprintLastTs = (System.currentTimeMillis() / 1000) - FINGERPRINT_EXPIRED_TIME + 2
    }

    private fun generateFingerprintData(context: Context): FingerPrint {
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

        val (lat, lon) = getLocationFromCache(context)

        val fp = FingerPrint(
            unique_id = DeviceInfo.getAdsId(context),
            device_name = deviceName,
            user_dname = DeviceInfo.getUserDeviceName(context),
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
            location_latitude = lat.toString(),
            location_longitude = lon.toString(),
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
            installer = context.packageManager.getInstallerPackageName(context.packageName) ?: "",
            accessType = getNetworkTypeInt(context)
        )
        return fp
    }

    private fun generateNewFingerprintData(context: Context): FingerPrintNew {
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
        val isEnableGetWidevineId = FirebaseRemoteConfigImpl(context).getBoolean(
            RemoteConfigKey.ANDROID_ENABLE_GENERATE_WIDEVINE_ID,
            true
        )
        val isEnableGetWidevineIdSuspend = FirebaseRemoteConfigImpl(context).getBoolean(
            RemoteConfigKey.ANDROID_ENABLE_GENERATE_WIDEVINE_ID_SUSPEND,
            true
        )
        val whitelistDisableWidevineId = FirebaseRemoteConfigImpl(context).getString(
            RemoteConfigKey.ANDROID_WHITELIST_DISABLE_GENERATE_WIDEVINE_ID,
            ""
        )
        val additionalInfoModel = AdditionalDeviceInfo.generate(
            context,
            isEnableGetWidevineId,
            isEnableGetWidevineIdSuspend,
            whitelistDisableWidevineId,
            getUserSession(context).userId
        )

        val (lat, lon) = getLocationFromCache(context)

        val fp = FingerPrintNew(
            unique_id = DeviceInfo.getAdsId(context),
            device_name = deviceName,
            user_dname = DeviceInfo.getUserDeviceName(context),
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
            location_latitude = lat.toString(),
            location_longitude = lon.toString(),
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
            time = additionalInfoModel.time.toString(),
            brand = additionalInfoModel.brand,
            product = additionalInfoModel.product,
            board = additionalInfoModel.board,
            cpuAbi = additionalInfoModel.cpuAbi,
            device = additionalInfoModel.device,
            versionName = additionalInfoModel.versionName,
            advertisingId = additionalInfoModel.advertisingId,
            wideVineId = additionalInfoModel.wideVineId,
            installer = context.packageManager.getInstallerPackageName(context.packageName) ?: "",
            accessType = getNetworkTypeInt(context)
        )
        return fp
    }

    // https://bytedance.sg.larkoffice.com/docx/LI0Qdh5m1ojHTNxhmQ1lWJgVg5f?from=from_copylink
    private fun getNetworkTypeInt(context: Context): Int {
        val networkType = NetworkUtils.getNetworkType(context)
        return when (networkType) {
            NetworkUtils.NetworkType.MOBILE_3G,
            NetworkUtils.NetworkType.MOBILE_3G_H,
            NetworkUtils.NetworkType.MOBILE_3G_HP -> 3 // NT3G
            NetworkUtils.NetworkType.MOBILE_4G -> 4 // NT4G
            NetworkUtils.NetworkType.MOBILE -> 2 // Mobile
            NetworkUtils.NetworkType.WIFI,
            NetworkUtils.NetworkType.WIFI_24GHZ,
            NetworkUtils.NetworkType.WIFI_5GHZ -> 1 // Wifi
            else -> 0 // Unknown
        }
    }

    private fun getLocationFromCache(ctx: Context): Pair<Double, Double> {
        val locationDetectorHelper = getLocationHelper(ctx)
        val deviceLocation = locationDetectorHelper.getLocationCache()
        initFlowOnce(ctx)
        return if (deviceLocation.hasLocation()) {
            deviceLocation.latitude to deviceLocation.longitude
        } else {
            DEFAULT_LATITUDE to DEFAULT_LONGITUDE
        }
    }

    /**
     * listen to location change from Room DB in form of Flow
     * If location is changed, trigger fingerprint to expired, thus make the fingerprint refreshed
     */
    private fun initFlowOnce(context: Context) {
        val fj = locationFlowJob
        try {
            if (locationFlow == null || fj == null || (fj.isCompleted)) {
                PersistentCacheManager(
                    context,
                    LOCATION_CACHE
                ).getFlow(
                    LocationDetectorHelper.PARAM_CACHE_DEVICE_LOCATION,
                    DeviceLocation::class.java,
                    DeviceLocation()
                ).also { it ->
                    locationFlow = it
                    // to be safe, we cancel previous job to make sure only 1 job exists
                    if (locationFlowJob != null) {
                        locationFlowJob?.cancel()
                    }
                    locationFlowJob = launch {
                        it.filter { it != null && it.hasLocation() }.map {
                            (it?.latitude ?: 0) to ((it?.longitude) ?: 0)
                        }.distinctUntilChanged().collect {
                            expireFingerprint()
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            FirebaseCrashlytics.getInstance().recordException(ex)
        }
    }

    private fun getLocationHelper(ctx: Context): LocationDetectorHelper {
        return locationDetectorHelper
            ?: LocationDetectorHelper(ctx).also {
                locationDetectorHelper = it
            }
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

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + CoroutineExceptionHandler { _, _ -> }

}
