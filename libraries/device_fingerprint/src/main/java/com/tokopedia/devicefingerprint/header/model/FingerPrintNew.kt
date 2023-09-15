package com.tokopedia.devicefingerprint.header.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FingerPrintNew(
    @SerializedName("device_model")
    @Expose
    private val deviceModel: String,

    @SerializedName("device_system")
    @Expose
    private val deviceSystem: String,

    @SerializedName("user_dname")
    @Expose
    private val userDname: String,

    @SerializedName("current_os")
    @Expose
    private val currentOs: String,

    @SerializedName("device_manufacturer")
    @Expose
    private val deviceManufacturer: String,

    @SerializedName("device_name")
    @Expose
    private val deviceName: String,

    @SerializedName("is_jailbroken_rooted")
    @Expose
    private val isJailbrokenRooted: Boolean,

    @SerializedName("timezone")
    @Expose
    private val timezone: String,

    @SerializedName("user_agent")
    @Expose
    private val userAgent: String,

    @SerializedName("is_emulator")
    @Expose
    private val isEmulator: Boolean,

    @SerializedName("is_tablet")
    @Expose
    private val isTablet: Boolean,

    @SerializedName("language")
    @Expose
    private val language: String,

    @SerializedName("carrier")
    @Expose
    private val carrier: String,

    @SerializedName("ssid")
    @Expose
    private val ssid: String,

    @SerializedName("screen_resolution")
    @Expose
    private val screenResolution: String,

    @SerializedName("location_latitude")
    @Expose
    private val locationLatitude: String,

    @SerializedName("location_longitude")
    @Expose
    private val locationLongitude: String,

    @SerializedName("is_nakama")
    @Expose
    private val isNakama: String,

    @SerializedName("unique_id")
    @Expose
    private val uniqueId: String,

    @SerializedName("inval")
    @Expose
    private val inval: String,

    @SerializedName("deviceMemoryClassCapacity")
    @Expose
    private val deviceMemoryClassCapacity: String,

    @SerializedName("availableProcessor")
    @Expose
    private val availableProcessor: String,

    @SerializedName("deviceDpi")
    @Expose
    private val deviceDpi: String,

    @SerializedName("packageName")
    @Expose
    private val packageName: String,

    @SerializedName("androidId")
    @Expose
    private val androidId: String,

    @SerializedName("isx86")
    @Expose
    private val isx86: Boolean,

    @SerializedName("pid")
    @Expose
    private var pid: String? = null,

    @SerializedName("uuid")
    @Expose
    private val uuid: String,

    @SerializedName("installer")
    @Expose
    private val installer: String,

    @SerializedName("time")
    @Expose
    private val time: String,

    @SerializedName("brand")
    @Expose
    private val brand: String,

    @SerializedName("product")
    @Expose
    private val product: String,

    @SerializedName("board")
    @Expose
    private val board: String,

    @SerializedName("cpuAbi")
    @Expose
    private val cpuAbi: String,

    @SerializedName("device")
    @Expose
    private val device: String,

    @SerializedName("versionName")
    @Expose
    private val versionName: String,

    @SerializedName("advertisingId")
    @Expose
    private val advertisingId: String,

    @SerializedName("wideVineId")
    @Expose
    private val wideVineId: String
) {

    fun hasUniqueId() = uniqueId.isNotEmpty()
}
