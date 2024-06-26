package com.tokopedia.tradein.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Laku6DeviceModel(
    @SerializedName("imei")
    var imei: String = "",
    @SerializedName("app_version")
    var appVersion: String = "",
    @SerializedName("brand")
    var brand: String = "",
    @SerializedName("campaign_id")
    var campaignId: String = "",
    @SerializedName("device")
    var device: String = "",
    @SerializedName("device_signature")
    var deviceSignature: String = "",
    @SerializedName("model")
    var model: String = "",
    @SerializedName("os_name")
    var osName: String = "",
    @SerializedName("os_version")
    var osVersion: String = "",
    @SerializedName("raw_ram")
    var rawRam: Long,
    @SerializedName("raw_storage")
    var rawStorage: String = "",
    @SerializedName("root_detected")
    var rootDetected: Boolean,
    @SerializedName("serial")
    var serial: String = "",
    @SerializedName("session_id")
    var sessionId: String = "",
    @SerializedName("skip_checking_price")
    var skipCheckingPrice: Boolean,
    @SerializedName("tokopedia_test_type")
    var tokopediaTestType: String = "",
    @SerializedName("trace_id")
    var traceId: String = "",
    @SerializedName("unique_code")
    var uniqueCode: String = "",
    var modelInfoBase64 : String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imei)
        parcel.writeString(appVersion)
        parcel.writeString(brand)
        parcel.writeString(campaignId)
        parcel.writeString(device)
        parcel.writeString(deviceSignature)
        parcel.writeString(model)
        parcel.writeString(osName)
        parcel.writeString(osVersion)
        parcel.writeLong(rawRam)
        parcel.writeString(rawStorage)
        parcel.writeByte(if (rootDetected) 1 else 0)
        parcel.writeString(serial)
        parcel.writeString(sessionId)
        parcel.writeByte(if (skipCheckingPrice) 1 else 0)
        parcel.writeString(tokopediaTestType)
        parcel.writeString(traceId)
        parcel.writeString(uniqueCode)
        parcel.writeString(modelInfoBase64)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Laku6DeviceModel> {
        override fun createFromParcel(parcel: Parcel): Laku6DeviceModel {
            return Laku6DeviceModel(parcel)
        }

        override fun newArray(size: Int): Array<Laku6DeviceModel?> {
            return arrayOfNulls(size)
        }
    }
}