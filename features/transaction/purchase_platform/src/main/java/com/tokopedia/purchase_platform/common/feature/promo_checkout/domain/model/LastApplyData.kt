package com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 2020-03-03.
 */
data class LastApplyData (
    var code: String = "",
    var additionalInfoMsg: String = "",
    var additionalInfoDetailMsg: String = "",
    var errorDetailMsg: String = "",
    var errorDetailDesc: String = "",
    var emptyCartInfoImgUrl: String = "",
    var emptyCartInfoMsg: String = "",
    var emptyCartInfoDetail: String = "",
    var listRedPromos: List<String> = listOf(),
    var finalBenefitText: String = "",
    var finalBenefitAmount: String = "",
    var globalPromoCode: String = "",
    var globalPromoSuccess: Boolean = false
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
            parcel.createStringArrayList() ?: listOf(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(code)
        parcel.writeString(additionalInfoMsg)
        parcel.writeString(additionalInfoDetailMsg)
        parcel.writeString(errorDetailMsg)
        parcel.writeString(errorDetailDesc)
        parcel.writeString(emptyCartInfoImgUrl)
        parcel.writeString(emptyCartInfoMsg)
        parcel.writeString(emptyCartInfoDetail)
        parcel.writeStringList(listRedPromos)
        parcel.writeString(finalBenefitText)
        parcel.writeString(finalBenefitAmount)
        parcel.writeString(globalPromoCode)
        parcel.writeByte(if (globalPromoSuccess) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LastApplyData> {
        override fun createFromParcel(parcel: Parcel): LastApplyData {
            return LastApplyData(parcel)
        }

        override fun newArray(size: Int): Array<LastApplyData?> {
            return arrayOfNulls(size)
        }
    }
}