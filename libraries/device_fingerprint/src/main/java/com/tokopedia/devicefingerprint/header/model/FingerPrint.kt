package com.tokopedia.devicefingerprint.header.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FingerPrint(
    @SerializedName("device_model")
    @Expose
    private val device_model: String,

    @SerializedName("device_system")
    @Expose
    private val device_system: String,

    @SerializedName("current_os")
    @Expose
    private val current_os: String,

    @SerializedName("device_manufacturer")
    @Expose
    private val device_manufacturer: String,

    @SerializedName("device_name")
    @Expose
    private val device_name: String,

    @SerializedName("is_jailbroken_rooted")
    @Expose
    private val is_jailbroken_rooted: Boolean,

    @SerializedName("timezone")
    @Expose
    private val timezone: String,

    @SerializedName("user_agent")
    @Expose
    private val user_agent: String,

    @SerializedName("is_emulator")
    @Expose
    private val is_emulator: Boolean,

    @SerializedName("is_tablet")
    @Expose
    private val is_tablet: Boolean,

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
    private val screen_resolution: String,

    @SerializedName("location_latitude")
    @Expose
    private val location_latitude: String,

    @SerializedName("location_longitude")
    @Expose
    private val location_longitude: String,

    @SerializedName("is_nakama")
    @Expose
    private val is_nakama: String,

    @SerializedName("unique_id")
    @Expose
    private val unique_id: String,

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
    private val uuid: String)