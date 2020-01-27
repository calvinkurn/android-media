package com.tokopedia.logisticdata.data.entity.address

import android.os.Parcel
import android.os.Parcelable

data class LocationDataModel(
        var addrId: Int = 0,
        var addrName: String = "",
        var address1: String = "",
        var address2: String = "",
        var city: Int = 0,
        var cityName: String = "",
        var country: String = "",
        var district: Int = 0,
        var districtName: String = "",
        var latitude: String = "",
        var longitude: String = "",
        var openingHours: String = "",
        var phone: String = "",
        var postalCode: String = "",
        var province: Int = 0,
        var provinceName: String = "",
        var receiverName: String = "",
        var status: Int = 0,
        var storeCode: String = "",
        var storeDistance: String = "",
        var type: Int = 0
) : Parcelable {
        constructor(source: Parcel) : this(
                source.readInt(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readInt(),
                source.readString(),
                source.readString(),
                source.readInt(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readInt(),
                source.readString(),
                source.readString(),
                source.readInt(),
                source.readString(),
                source.readString(),
                source.readInt()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                writeInt(addrId)
                writeString(addrName)
                writeString(address1)
                writeString(address2)
                writeInt(city)
                writeString(cityName)
                writeString(country)
                writeInt(district)
                writeString(districtName)
                writeString(latitude)
                writeString(longitude)
                writeString(openingHours)
                writeString(phone)
                writeString(postalCode)
                writeInt(province)
                writeString(provinceName)
                writeString(receiverName)
                writeInt(status)
                writeString(storeCode)
                writeString(storeDistance)
                writeInt(type)
        }

        companion object {
                @JvmField
                val CREATOR: Parcelable.Creator<LocationDataModel> = object : Parcelable.Creator<LocationDataModel> {
                        override fun createFromParcel(source: Parcel): LocationDataModel = LocationDataModel(source)
                        override fun newArray(size: Int): Array<LocationDataModel?> = arrayOfNulls(size)
                }
        }
}