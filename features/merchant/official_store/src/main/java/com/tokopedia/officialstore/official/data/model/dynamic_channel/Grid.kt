package com.tokopedia.officialstore.official.data.model.dynamic_channel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Grid(
        @Expose @SerializedName("freeOngkir") val freeOngkir: FreeOngkir?,
        @Expose @SerializedName("id") val id: Long,
        @Expose @SerializedName("name") val name: String,
        @Expose @SerializedName("applink") val applink: String,
        @Expose @SerializedName("price") val price: String,
        @Expose @SerializedName("slashedPrice") val slashedPrice: String,
        @Expose @SerializedName("discount") val discount: String,
        @Expose @SerializedName("imageUrl") val imageUrl: String,
        @Expose @SerializedName("label") val label: String,
        @Expose @SerializedName("soldPercentage") val soldPercentage: Long,
        @Expose @SerializedName("attribution") val attribution: String,
        @Expose @SerializedName("productClickUrl") val productClickUrl: String,
        @Expose @SerializedName("impression") val impression: String,
        @Expose @SerializedName("cashback") val cashback: String
) : Parcelable {

    private constructor(parcel: Parcel) : this(
            freeOngkir = parcel.readParcelable(FreeOngkir::class.java.classLoader),
            id = parcel.readLong(),
            name = parcel.readString() ?: "",
            applink = parcel.readString() ?: "",
            price = parcel.readString() ?: "",
            slashedPrice = parcel.readString() ?: "",
            discount = parcel.readString() ?: "",
            imageUrl = parcel.readString() ?: "",
            label = parcel.readString() ?: "",
            soldPercentage = parcel.readLong(),
            attribution = parcel.readString() ?: "",
            productClickUrl = parcel.readString() ?: "",
            impression = parcel.readString() ?: "",
            cashback = parcel.readString() ?: ""
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.run {
            writeParcelable(freeOngkir, flags)
            writeLong(id)
            writeString(name)
            writeString(applink)
            writeString(price)
            writeString(slashedPrice)
            writeString(discount)
            writeString(imageUrl)
            writeString(label)
            writeLong(soldPercentage)
            writeString(attribution)
            writeString(productClickUrl)
            writeString(impression)
            writeString(cashback)
        }
    }

    override fun describeContents() = 0

    companion object {
        @JvmField val CREATOR = createParcel { Grid(it) }
    }
}
