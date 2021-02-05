package com.tokopedia.devicefingerprint.submitdevice.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.location.Location
import android.net.wifi.WifiManager
import android.os.Build
import android.os.SystemClock
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.device.info.DeviceInfo
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.devicefingerprint.submitdevice.model.Screen
import com.tokopedia.devicefingerprint.submitdevice.payload.DeviceInfoPayload
import com.tokopedia.encryption.security.sha256
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber
import java.io.File
import java.lang.reflect.Field
import java.math.BigInteger
import java.net.InetAddress
import java.nio.ByteOrder
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.sqrt


class DeviceInfoPayloadCreator @Inject constructor(
        val context: Context,
        val userSession: UserSessionInterface,
        val fusedLocationClient: FusedLocationProviderClient
) {

    private val TIMESTAMP_FORMAT = "dd/MM/yyyy HH:mm:ss"
    private val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    private val remoteConfig = FirebaseRemoteConfigImpl(context)

    companion object {
        const val KEY_SEND_HIGH_RISK_APPS = "android_mainapp_send_high_risk_apps"
    }

    suspend fun createDevicePayload(): DeviceInfoPayload {
        val location = getLocation()
        val latitude = location?.latitude ?: 0f
        val longitude = location?.longitude ?: 0f

        return DeviceInfoPayload(
                deviceOs = "android",
                isRooted = isRooted(),
                userAgent = getUserAgent(),
                isTablet = isTablet(),
                adsId = getAdsId(),
                androidId = getAndroidId(),
                serialNumber = getSerialNumber(),
                buildFingerprint = Build.FINGERPRINT,
                buildId = Build.ID,
                buildVersionIncremental = Build.VERSION.INCREMENTAL,
                appVersion = Build.VERSION.RELEASE,
                isFromPlayStore = isFromPlayStore(),
                uuid = DeviceInfo.getUUID(context),
                userId = userSession.userId.toInt(),
                deviceModel = Build.MODEL,
                deviceManufacturer = Build.MANUFACTURER,
                timezone = TimeZone.getDefault().displayName,
                screenResolution = DeviceScreenInfo.getScreenResolution(context),
                language = Locale.getDefault().toString(),
                ssid = DeviceConnectionInfo.getSSID(context),
                deviceCarrier = DeviceConnectionInfo.getCarrierName(context),
                latitude = latitude.toString(),
                longitude = longitude.toString(),
                cpuInfo = getCpuInfo() ?: "",
                buildDisplay = Build.DISPLAY,
                buildBoard = Build.BOARD,
                buildSupportAbis = getSupportAbis(),
                buildHost = Build.HOST,
                packageName = context.packageName,
                wifiIp = getWifiIp(),
                sysFontMap = getSystemFontMap() ?: "",
                firstInstallTime = getFirstInstallTime(context),
                lastUpdateTime = getLastUpdateTime(context),
                timeSinceBoot = SystemClock.elapsedRealtime(),
                firstBootTime = System.currentTimeMillis() - SystemClock.elapsedRealtime(),
                screenInfo = getScreenInfo(),
                mcc = getMcc(),
                mnc = getMnc(),
                bootCount = getBootCount(),
                permissions = getPermissions(context),
                appList = if (remoteConfig.getBoolean(KEY_SEND_HIGH_RISK_APPS, true)) {
                    getEncodedInstalledApps(context).joinToString(separator = ",")
                } else {
                    ""
                }
        )
    }

    private suspend fun getLocation(): Location? {
        val isGrantedFineLocation = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val isGrantedCoarseLocation = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (isGrantedFineLocation && isGrantedCoarseLocation) {
            return suspendCoroutine { cont ->
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    cont.resume(location)
                }
                fusedLocationClient.lastLocation.addOnFailureListener { exception ->
                    cont.resumeWithException(exception)
                }
            }
        } else {
            return null
        }
    }

    private fun getEncodedInstalledApps(context: Context): List<String> {
        return try {
            val pm = context.packageManager
            val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            val packageList = mutableListOf<String>()
            for (packageInfo in packages) {
                packageList.add(packageInfo.packageName.sha256())
            }
            return packageList
        } catch (e: Exception) {
            listOf()
        }
    }

    private fun getScreenInfo(): String {
        try {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val screen: Screen
            val realMetrics = DisplayMetrics()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealMetrics(realMetrics)
            } else {
                display.getMetrics(realMetrics)
            }
            val realWidth = realMetrics.widthPixels
            val realHeight = realMetrics.heightPixels
            val densityDpi = realMetrics.densityDpi
            val xdpi = realMetrics.xdpi
            val ydpi = realMetrics.ydpi
            screen = Screen(realWidth, realHeight, densityDpi, xdpi, ydpi)
            // orientation (either ORIENTATION_LANDSCAPE, ORIENTATION_PORTRAIT)
            val wi = screen.realWidth.toDouble() / screen.xdpi.toDouble()
            val hi = screen.realHeight.toDouble() / screen.ydpi.toDouble()
            val x = Math.pow(wi, 2.0)
            val y = Math.pow(hi, 2.0)
            val screenInches = sqrt(x + y)

            return "${screen.realWidth}|${screen.realHeight}|${screenInches}|${screen.densityDpi}"
        } catch (e: Exception) {
            return ""
        }
    }

    private fun getWifiIp(): String {
        var result = ""
        try {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            if (wifiManager.connectionInfo != null) {
                var ipAddress = wifiManager.connectionInfo.ipAddress
                // Convert little-endian to big-endianif needed
                if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                    ipAddress = Integer.reverseBytes(ipAddress)
                }
                val ipByteArray = BigInteger.valueOf(ipAddress.toLong()).toByteArray()
                result = InetAddress.getByAddress(ipByteArray).hostAddress
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
        return result
    }

    private fun getSupportAbis(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Build.SUPPORTED_ABIS.joinToString(",")
        } else {
            ""
        }
    }

    private fun getBootCount(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Settings.Global.getInt(context.contentResolver, Settings.Global.BOOT_COUNT)
        } else {
            1
        }
    }

    private fun getMnc(): String {
        val networkOperator = telephonyManager.networkOperator
        return if (networkOperator.isEmpty()) {
            ""
        } else {
            networkOperator.substring(3)
        }
    }

    private fun getMcc(): String {
        val networkOperator = telephonyManager.networkOperator
        return if (telephonyManager.networkCountryIso.isEmpty()) {
            ""
        } else {
            networkOperator.substring(0, 3)
        }
    }

    private fun isFromPlayStore(): Boolean {
        return try {
            val installer = context.packageManager.getInstallerPackageName(context.packageName)
            installer != null && installer.startsWith("com.android.vending")
        } catch (e: Exception) {
            false
        }
    }

    @SuppressLint("MissingPermission")
    private fun getBuildSerial(): String {
        return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            Build.SERIAL
        } else {
            if (ContextCompat.checkSelfPermission(
                            context, Manifest.permission.READ_PHONE_STATE
                    ) == PackageManager.PERMISSION_GRANTED
            ) {
                Build.getSerial()
            } else {
                ""
            }
        }
    }

    private fun getAndroidId(): String {
        return DeviceInfo.getAndroidId(context)
    }

    private fun getAdsId(): String {
        return DeviceInfo.getAdsId(context)
    }

    private fun isTablet(): Boolean {
        return DeviceScreenInfo.isTablet(context)
    }

    private fun getUserAgent(): String {
        return System.getProperty("http.agent") ?: ""
    }

    private fun isRooted(): Boolean {
        return DeviceInfo.isRooted()
    }

    private fun getSerialNumber(): String {
        var serialNumber = ""
        try {
            val c = Class.forName("android.os.SystemProperties")
            val get = c.getMethod("get", String::class.java)
            serialNumber = get.invoke(c, "sys.serialnumber") as String
            if (serialNumber == "") {
                serialNumber = get.invoke(c, "ril.serialnumber") as String
            }
            if (serialNumber == "") {
                serialNumber = get.invoke(c, "gsm.sn1") as String
            }
            if (serialNumber == "") {
                serialNumber = get.invoke(c, "ro.serialno") as String
            }
            return serialNumber
        } catch (e: Exception) {
            Timber.e(e)
        }
        return serialNumber
    }

    private fun getLastUpdateTime(context: Context): String {
        val pm = context.packageManager
        val appInfo = pm.getApplicationInfo(context.packageName, 0)
        val appFile = appInfo.sourceDir
        val epochTime = File(appFile).lastModified() //Epoch Time
        return SimpleDateFormat(TIMESTAMP_FORMAT, Locale.getDefault()).format(Date(epochTime))
    }

    fun getSystemFontMap(): String? {
        val systemFontMap: Map<String, Typeface?>?
        var fonts = ""
        try { //Typeface typeface = Typeface.class.newInstance();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                val f: Field
                f = Typeface::class.java.getDeclaredField("sSystemFontMap")
                f.isAccessible = true
                systemFontMap = f[typeface] as Map<String, Typeface?>
                for ((key) in systemFontMap) fonts =
                        "$fonts$key,"
            } else {
                val path = "/system/fonts"
                val file = File(path)
                val ff = file.listFiles()
                if (ff != null) {
                    for (f in ff) {
                        var fname = f.name
                        val pos = fname.lastIndexOf(".")
                        if (pos > 0) fname = fname.substring(0, pos)
                        fonts = "$fonts$fname,"
                    }
                }
            }
        } catch (e: Exception) {
            e.toString()
        }
        return if (fonts.isNotEmpty()) fonts.substring(0, fonts.length - 1) else ""
    }

    fun getPermissions(context: Context): List<String> {
        val permissions: MutableList<String> = ArrayList()
        try {
            val pm = context.packageManager
            val packageInfo = pm.getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)
            for (i in packageInfo.requestedPermissions.indices) {
                if (packageInfo.requestedPermissionsFlags[i] and PackageInfo.REQUESTED_PERMISSION_GRANTED != 0) {
                    var permission = packageInfo.requestedPermissions[i]
                    permission = permission.substring(permission.lastIndexOf(".") + 1).toLowerCase()
                    permissions.add(permission)
                }
            }
        } catch (e: Exception) {
            Timber.e("Error when getting permissions. Exception: $e")
        }
        return permissions
    }

    fun getCpuInfo(): String? {
        return try {
            val data = arrayOf("/system/bin/cat", "/proc/cpuinfo")
            readSysFile(data)?.replace("[^A-Za-z0-9 \\s\\-_.]+".toRegex(), "")
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun readSysFile(data: Array<String>): String? {
        return try {
            var result: String? = ""
            val byteArr = ByteArray(1024)
            val processBuilder = ProcessBuilder(*data)
            val process = processBuilder.start()
            val inputStream = process.inputStream
            while (inputStream.read(byteArr) != -1) {
                result += String(byteArr)
            }
            inputStream.close()
            result
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun getFirstInstallTime(context: Context): String {
        return try {
            getReadableTimeStamp(
                    context.packageManager.getPackageInfo(context.packageName, 0).firstInstallTime
            )
        } catch (e: Exception) {
            ""
        }
    }

    fun getReadableTimeStamp(timeStamp: Long): String {
        return SimpleDateFormat(TIMESTAMP_FORMAT, Locale.getDefault()).format(Date(timeStamp))
    }

}
