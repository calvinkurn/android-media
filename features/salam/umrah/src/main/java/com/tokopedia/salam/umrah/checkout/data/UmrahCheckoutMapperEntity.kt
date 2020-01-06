package com.tokopedia.salam.umrah.checkout.data


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.salam.umrah.common.data.UmrahProductModel

/**
 * @author by firman on 27/11/2019
 */

class UmrahCheckoutMapperEntity(
    @SerializedName("checkoutPDP")
    @Expose
    val checkoutPDP : UmrahProductModel.UmrahProduct = UmrahProductModel.UmrahProduct(),
    @SerializedName("user")
    @Expose
    val user: ContactUser = ContactUser(),
    @SerializedName("summaryPayment")
    @Expose
    val summaryPayment: UmrahCheckoutSummaryEntity = UmrahCheckoutSummaryEntity(),
    @SerializedName("paymentOptions")
    @Expose
    val paymentOptions: UmrahCheckoutPaymentOptionsEntity = UmrahCheckoutPaymentOptionsEntity(),
    @SerializedName("termCondition")
    @Expose
    val termCondition: UmrahCheckoutTermConditionsEntity = UmrahCheckoutTermConditionsEntity()
)


class ContactUser(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("name")
        @Expose
        var name: String = "",
        @SerializedName("email")
        @Expose
        var email: String = "",
        @SerializedName("phoneNumber")
        @Expose
        var phoneNumber : String = "",
        @SerializedName("phoneCode")
        @Expose
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


