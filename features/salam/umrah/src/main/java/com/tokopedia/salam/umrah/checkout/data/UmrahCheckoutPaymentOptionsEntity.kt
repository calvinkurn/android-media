package com.tokopedia.salam.umrah.checkout.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author by firman on 27/11/2019
 */

class UmrahCheckoutPaymentOptionsEntity(
        @SerializedName("umrahPaymentOptions")
        @Expose
        val umrahPaymentOptions: UmrahPaymentOptions = UmrahPaymentOptions()

)

class UmrahPaymentOptions(
        @SerializedName("paymentOptions")
        @Expose
        val paymentOptions: List<PaymentOptions> = emptyList(),
        @SerializedName("defaultOption")
        @Expose
        val defaultOption: Int = 0
)

class PaymentOptions(
        @SerializedName("price")
        @Expose
        val price: Int = 0,
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("desc")
        @Expose
        val desc: String = "",
        @SerializedName("defaultOption")
        @Expose
        val defaultOption: Int = 0,
        @SerializedName("schemes")
        @Expose
        val schemes: ArrayList<Schemes> = arrayListOf()
)

@Parcelize
class Schemes(
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("price")
        @Expose
        val price: Int = 0,
        @SerializedName("firstDueDate")
        @Expose
        val firstDueDate: String = "",
        @SerializedName("terms")
        @Expose
        val terms: List<Term> = arrayListOf()
) : Parcelable

@Parcelize
class Term(
        @SerializedName("type")
        @Expose
        val type: Int = 0,
        @SerializedName("price")
        @Expose
        val price: Int = 0,
        @SerializedName("dueDate")
        @Expose
        val dueDate: String = ""
) : Parcelable