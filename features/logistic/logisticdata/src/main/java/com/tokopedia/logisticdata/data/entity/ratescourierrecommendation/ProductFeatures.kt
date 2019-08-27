package com.tokopedia.logisticdata.data.entity.ratescourierrecommendation

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class ProductFeatures(
        @SerializedName("ontime_delivery_guarantee")
        var ontimeDeliveryGuarantee: OntimeDeliveryGuarantee = OntimeDeliveryGuarantee()
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readParcelable<OntimeDeliveryGuarantee>(OntimeDeliveryGuarantee::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(ontimeDeliveryGuarantee, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ProductFeatures> = object : Parcelable.Creator<ProductFeatures> {
            override fun createFromParcel(source: Parcel): ProductFeatures = ProductFeatures(source)
            override fun newArray(size: Int): Array<ProductFeatures?> = arrayOfNulls(size)
        }
    }
}

data class OntimeDeliveryGuarantee(
        @SerializedName("available")
        var available: Boolean = false,
        @SerializedName("icon_url")
        var iconUrl: String = "",
        @SerializedName("text_detail")
        var textDetail: String = "",
        @SerializedName("text_label")
        var textLabel: String = "",
        @SerializedName("value")
        var value: Int = 0
) : Parcelable {
    constructor(source: Parcel) : this(
            1 == source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt((if (available) 1 else 0))
        writeString(iconUrl)
        writeString(textDetail)
        writeString(textLabel)
        writeInt(value)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<OntimeDeliveryGuarantee> = object : Parcelable.Creator<OntimeDeliveryGuarantee> {
            override fun createFromParcel(source: Parcel): OntimeDeliveryGuarantee = OntimeDeliveryGuarantee(source)
            override fun newArray(size: Int): Array<OntimeDeliveryGuarantee?> = arrayOfNulls(size)
        }
    }
}