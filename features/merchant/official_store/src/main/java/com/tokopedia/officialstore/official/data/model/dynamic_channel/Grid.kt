package com.tokopedia.officialstore.official.data.model.dynamic_channel

import android.os.Parcel
import android.os.Parcelable

data class Grid(
        val id: Long,
        val name: String,
        val applink: String,
        val price: String,
        val slashedPrice: String,
        val discount: String,
        val imageUrl: String,
        val label: String,
        val soldPercentage: Long,
        val attribution: String,
        val productClickUrl: String,
        val impression: String,
        val cashback: String
) : Parcelable {

    private constructor(parcel: Parcel) : this(
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
