package com.tokopedia.otp.notif.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Ade Fulki on 22/09/20.
 */

data class DeviceStatusPushNotifPojo(
        @SerializedName("DeviceStatusPushnotif")
        @Expose
        var data: DeviceStatusPushNotifData = DeviceStatusPushNotifData()
)

data class DeviceStatusPushNotifData(
        @SerializedName("success")
        @Expose
        var success: Boolean = false,
        @SerializedName("errorMessage")
        @Expose
        var errorMessage: String = "",
        @SerializedName("isTrusted")
        @Expose
        var isTrusted: Boolean = false,
        @SerializedName("isActive")
        @Expose
        var isActive: Boolean = false,
        @SerializedName("listDevices")
        @Expose
        var listDevices: ArrayList<ListDeviceData> = arrayListOf()
) : Serializable

data class ListDeviceData(
        @SerializedName("deviceName")
        @Expose
        var deviceName: String = ""
) : Serializable