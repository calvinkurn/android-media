package com.tokopedia.payment.setting.list.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.payment.setting.list.view.adapter.SettingListPaymentAdapterTypeFactory

open class SettingListPaymentModel() : Visitable<SettingListPaymentAdapterTypeFactory>, Parcelable {
    override fun type(typeFactory: SettingListPaymentAdapterTypeFactory?): Int {
        return typeFactory?.type(this)?:0
    }

    @SerializedName("card_type")
    @Expose
    var cardType: String? = null
    @SerializedName("expiry_month")
    @Expose
    var expiryMonth: String? = null
    @SerializedName("expiry_year")
    @Expose
    var expiryYear: String? = null
    @SerializedName("masked_number")
    @Expose
    var maskedNumber: String? = null
    @SerializedName("token_id")
    @Expose
    var tokenId: String? = null

    constructor(parcel: Parcel) : this() {
        cardType = parcel.readString()
        expiryMonth = parcel.readString()
        expiryYear = parcel.readString()
        maskedNumber = parcel.readString()
        tokenId = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cardType)
        parcel.writeString(expiryMonth)
        parcel.writeString(expiryYear)
        parcel.writeString(maskedNumber)
        parcel.writeString(tokenId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SettingListPaymentModel> {
        override fun createFromParcel(parcel: Parcel): SettingListPaymentModel {
            return SettingListPaymentModel(parcel)
        }

        override fun newArray(size: Int): Array<SettingListPaymentModel?> {
            return arrayOfNulls(size)
        }
    }

}
