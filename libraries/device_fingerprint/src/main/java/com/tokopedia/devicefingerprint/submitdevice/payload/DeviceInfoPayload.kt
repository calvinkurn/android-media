package com.tokopedia.devicefingerprint.submitdevice.payload

import com.google.gson.annotations.SerializedName

data class DeviceInfoPayload(
        @SerializedName("device_os") val deviceOs: String = "android",
        @SerializedName("is_rooted") val isRooted: Boolean = false,
        @SerializedName("user_agent") val userAgent: String,
        @SerializedName("is_tablet") val isTablet: Boolean = false,
        @SerializedName("ads_id") val adsId: String,
        @SerializedName("android_id") val androidId: String,
        @SerializedName("sys_serial_number") val serialNumber: String,
        @SerializedName("build_fingerprint") val buildFingerprint: String,
        @SerializedName("build_id") val buildId: String,
        @SerializedName("build_version_incremental") val buildVersionIncremental: String,
        @SerializedName("app_version") val appVersion: String,
        @SerializedName("is_from_playstore") val isFromPlayStore: Boolean = true,
        @SerializedName("uuid") val uuid: String,
        @SerializedName("user_id") val userId: Long,
        @SerializedName("dvmod") val deviceModel: String,
        @SerializedName("dvmanu") val deviceManufacturer: String,
        @SerializedName("tzone") val timezone: String,
        @SerializedName("scrres") val screenResolution: String,
        @SerializedName("lang") val language: String,
        @SerializedName("ssid") val ssid: String,
        @SerializedName("carri") val deviceCarrier: String,
        @SerializedName("lat") val latitude: String,
        @SerializedName("long") val longitude: String,
        @SerializedName("cpu") val cpuInfo: String,
        @SerializedName("bdispl") val buildDisplay: String,
        @SerializedName("bboard") val buildBoard: String,
        @SerializedName("bsupabis") val buildSupportAbis: String,
        @SerializedName("bhost") val buildHost: String,
        @SerializedName("pkgname") val packageName: String,
        @SerializedName("wifiip") val wifiIp: String,
        @SerializedName("font") val sysFontMap: String,
        @SerializedName("1stinstall") val firstInstallTime: String,
        @SerializedName("lastupdate") val lastUpdateTime: String,
        @SerializedName("tsinceboot") val timeSinceBoot: Long,
        @SerializedName("1stboot") val firstBootTime: Long,
        @SerializedName("scrinf") val screenInfo: String,
        @SerializedName("mcc") val mcc: String,
        @SerializedName("mnc") val mnc: String,
        @SerializedName("bootcount") val bootCount: Int,
        @SerializedName("permissionapp") val permissions: List<String>,
        @SerializedName("app_list") val appList: String
)
