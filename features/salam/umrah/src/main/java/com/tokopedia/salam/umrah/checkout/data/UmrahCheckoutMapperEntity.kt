package com.tokopedia.salam.umrah.checkout.data


import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.salam.umrah.common.data.UmrahProductModel

class UmrahCheckoutMapperEntity(
    val checkoutPDP : UmrahProductModel.UmrahProduct = UmrahProductModel.UmrahProduct(),
    val user: ContactUser = ContactUser(),
    val summaryPayment: UmrahCheckoutSummaryEntity = UmrahCheckoutSummaryEntity(),
    val paymentOptions: UmrahCheckoutPaymentOptionsEntity = UmrahCheckoutPaymentOptionsEntity(),
    val termCondition: UmrahCheckoutTermConditionsEntity = UmrahCheckoutTermConditionsEntity()
)


class ContactUser(
        val id: String = "",
        var name: String = "",
        var email: String = "",
        var phoneNumber : String = "",
        var phoneCode : Int = 62
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(phoneNumber)
        parcel.writeInt(phoneCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ContactUser> {
        override fun createFromParcel(parcel: Parcel): ContactUser {
            return ContactUser(parcel)
        }

        override fun newArray(size: Int): Array<ContactUser?> {
            return arrayOfNulls(size)
        }
    }
}


