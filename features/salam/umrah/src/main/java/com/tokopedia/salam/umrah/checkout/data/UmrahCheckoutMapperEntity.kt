package com.tokopedia.salam.umrah.checkout.data


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.salam.umrah.common.data.UmrahProductModel
import kotlinx.android.parcel.Parcelize

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

@Parcelize
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
) : Parcelable


