package com.tokopedia.hotel.booking.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HotelPropertyRoom (

    @SerializedName("roomID")
    @Expose
    val roomID: String = "",

    @SerializedName("isBreakFastIncluded")
    @Expose
    val isBreakFastIncluded: Boolean = false,

    @SerializedName("maxOccupancy")
    @Expose
    val maxOccupancy: Int = 0,

    @SerializedName("roomName")
    @Expose
    val roomName: String = "",

    @SerializedName("isNeedDeposit")
    @Expose
    val isNeedDeposit: Boolean = false,

    @SerializedName("isRefundAble")
    @Expose
    val isRefundAble: Boolean = false,

    @SerializedName("isCCRequired")
    @Expose
    val isCCRequired: Boolean = false,

    @SerializedName("isDirectPayment")
    @Expose
    val isDirectPayment: Boolean = false,

    @SerializedName("paymentTerms")
    @Expose
    val paymentTerms: PaymentTerms = PaymentTerms(),

    @SerializedName("roomPolicies")
    @Expose
    val roomPolicies: List<HotelRoomPolicy> = listOf(),

    @SerializedName("cancellationPolicies")
    @Expose
    val cancellationPolicies: CancellationPolicy = CancellationPolicy()

) {

    data class HotelRoomPolicy (

        @SerializedName("content")
        @Expose
        val content: String = "",

        @SerializedName("policyType")
        @Expose
        val policyType: String = ""

    )

    data class PaymentTerms (

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("cancellationDescription")
        @Expose
        val cancellationDescription: String = "",

        @SerializedName("prepaymentDescription")
        @Expose
        val prepaymentDescription: String = ""

    )

    data class CancellationPolicy (

        @SerializedName("title")
        @Expose
        var title: String = "",

        @SerializedName("content")
        @Expose
        var content: String = "",

        @SerializedName("isClickable")
        @Expose
        var isClickable: Boolean = false,

        @SerializedName("details")
        @Expose
        var details: List<CancellationPolicyDetail> = listOf()

    )

    data class CancellationPolicyDetail (

        @SerializedName("shortTitle")
        @Expose
        var shortTitle: String = "",

        @SerializedName("longTitle")
        @Expose
        var longTitle: String = "",

        @SerializedName("shortDesc")
        @Expose
        var shortDesc: String = "",

        @SerializedName("longDesc")
        @Expose
        var longDesc: String = "",

        @SerializedName("isActive")
        @Expose
        var isActive: Boolean = false

    )

}