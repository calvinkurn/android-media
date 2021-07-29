package com.tokopedia.common_tradein.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.logisticCommon.data.entity.address.Token

data class MoneyInKeroGetAddressResponse(
        @SerializedName("data")
        val data: ResponseData
) {
    data class ResponseData(
            @SerializedName("keroAddressCorner")
            val keroGetAddress: KeroGetAddress
    ) {
        data class KeroGetAddress(
                @SerializedName("data")
                val data: List<Data>,
                @SerializedName("config")
                val config: String,
                @SerializedName("server_process_time")
                val serverProcessTime: String,
                @SerializedName("status")
                val status: String,
                @SerializedName("token")
                val token: Token
        ) {
            data class Data(
                    @SerializedName("addr_id")
                    val addrId: Int,
                    @SerializedName("addr_name")
                    val addrName: String,
                    @SerializedName("address_1")
                    val address1: String,
                    @SerializedName("address_2")
                    val address2: String,
                    @SerializedName("city")
                    val city: Int,
                    @SerializedName("city_name")
                    val cityName: String,
                    @SerializedName("country")
                    val country: String,
                    @SerializedName("district")
                    val district: Int,
                    @SerializedName("district_name")
                    val districtName: String?,
                    @SerializedName("is_active")
                    val isActive: Boolean,
                    @SerializedName("is_primary")
                    val isPrimary: Boolean,
                    @SerializedName("is_whitelist")
                    val isWhitelist: Boolean,
                    @SerializedName("latitude")
                    val latitude: String?,
                    @SerializedName("longitude")
                    val longitude: String?,
                    @SerializedName("phone")
                    val phone: String,
                    @SerializedName("postal_code")
                    val postalCode: String,
                    @SerializedName("province")
                    val province: Int,
                    @SerializedName("province_name")
                    val provinceName: String?,
                    @SerializedName("receiver_name")
                    val receiverName: String,
                    @SerializedName("status")
                    val status: Int
            ) : Parcelable {
                constructor(parcel: Parcel) : this(
                        parcel.readInt(),
                        parcel.readString()?:"",
                        parcel.readString()?:"",
                        parcel.readString()?:"",
                        parcel.readInt(),
                        parcel.readString()?:"",
                        parcel.readString()?:"",
                        parcel.readInt(),
                        parcel.readString(),
                        parcel.readByte() != 0.toByte(),
                        parcel.readByte() != 0.toByte(),
                        parcel.readByte() != 0.toByte(),
                        parcel.readString(),
                        parcel.readString(),
                        parcel.readString()?:"",
                        parcel.readString()?:"",
                        parcel.readInt(),
                        parcel.readString(),
                        parcel.readString()?:"",
                        parcel.readInt())

                override fun writeToParcel(parcel: Parcel, flags: Int) {
                    parcel.writeInt(addrId)
                    parcel.writeString(addrName)
                    parcel.writeString(address1)
                    parcel.writeString(address2)
                    parcel.writeInt(city)
                    parcel.writeString(cityName)
                    parcel.writeString(country)
                    parcel.writeInt(district)
                    parcel.writeString(districtName)
                    parcel.writeByte(if (isActive) 1 else 0)
                    parcel.writeByte(if (isPrimary) 1 else 0)
                    parcel.writeByte(if (isWhitelist) 1 else 0)
                    parcel.writeString(latitude)
                    parcel.writeString(longitude)
                    parcel.writeString(phone)
                    parcel.writeString(postalCode)
                    parcel.writeInt(province)
                    parcel.writeString(provinceName)
                    parcel.writeString(receiverName)
                    parcel.writeInt(status)
                }

                override fun describeContents(): Int {
                    return 0
                }

                companion object CREATOR : Parcelable.Creator<Data> {
                    override fun createFromParcel(parcel: Parcel): Data {
                        return Data(parcel)
                    }

                    override fun newArray(size: Int): Array<Data?> {
                        return arrayOfNulls(size)
                    }
                }
            }
        }
    }
}
