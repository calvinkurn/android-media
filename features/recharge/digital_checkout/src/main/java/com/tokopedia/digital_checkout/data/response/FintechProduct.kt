package com.tokopedia.digital_checkout.data.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FintechProduct(
        @SerializedName("transaction_type")
        @Expose
        var transactionType: String? = null,

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
        var info: FintechProductInfo? = null
) : Parcelable {
    @Parcelize
    data class FintechProductInfo(
            @SerializedName("title")
            @Expose
            var title: String? = null,
            @Expose
            var subtitle: String? = null,
            @SerializedName("link_text")
            @Expose
            var textLink: String? = null,
            @SerializedName("link_url")
            @Expose
            var urlLink: String? = null,
            @SerializedName("tooltip_text")
            @Expose
            var tooltipText: String? = null
    ) : Parcelable
}