package com.tokopedia.digital_checkout.data.response.getcart

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FintechProduct(
        @SerializedName("id")
        @Expose
        val id: Int = 0,

        @SerializedName("transaction_type")
        @Expose
        var transactionType: String = "",

        @SerializedName("tier_id")
        @Expose
        var tierId: Int = 0,

        @SerializedName("opt_in")
        @Expose
        var optIn: Boolean = false,

        @SerializedName("check_box_disabled")
        @Expose
        var checkBoxDisabled: Boolean = false,

        @SerializedName("allow_ovo_points")
        @Expose
        var allowOVOPoints: Boolean = false,

        @SerializedName("fintech_amount")
        @Expose
        var fintechAmount: Long = 0,

        @SerializedName("fintech_partner_amount")
        @Expose
        var fintechPartnerAmount: Long = 0,

        @SerializedName("info")
        @Expose
        var info: FintechProductInfo = FintechProductInfo()
) : Parcelable {

    @Parcelize
    data class FintechProductInfo(
            @SerializedName("title")
            @Expose
            var title: String = "",
            @Expose
            var subtitle: String = "",
            @SerializedName("link_text")
            @Expose
            var textLink: String = "",
            @SerializedName("link_url")
            @Expose
            var urlLink: String = "",
            @SerializedName("tooltip_text")
            @Expose
            var tooltipText: String = ""
    ) : Parcelable
}