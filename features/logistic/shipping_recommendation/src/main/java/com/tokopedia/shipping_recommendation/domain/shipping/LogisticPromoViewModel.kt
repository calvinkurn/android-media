package com.tokopedia.shipping_recommendation.domain.shipping

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fajarnuha on 29/03/19.
 */
data class LogisticPromoViewModel(val promoCode: String,
                                  val title: String,
                                  val description: String,
                                  val shipperName: String,
                                  val serviceId: Int,
                                  val shipperId: Int,
                                  val shipperProductId: Int,
                                  val shipperDesc: String,
                                  val disableText: String,
                                  val dialogMsg: String) : RatesViewModelType, Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(promoCode)
        writeString(title)
        writeString(description)
        writeString(shipperName)
        writeInt(serviceId)
        writeInt(shipperId)
        writeInt(shipperProductId)
        writeString(shipperDesc)
        writeString(disableText)
        writeString(dialogMsg)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LogisticPromoViewModel> = object : Parcelable.Creator<LogisticPromoViewModel> {
            override fun createFromParcel(source: Parcel): LogisticPromoViewModel = LogisticPromoViewModel(source)
            override fun newArray(size: Int): Array<LogisticPromoViewModel?> = arrayOfNulls(size)
        }
    }
}
