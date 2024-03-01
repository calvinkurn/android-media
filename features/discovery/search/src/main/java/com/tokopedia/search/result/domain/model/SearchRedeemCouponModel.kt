package com.tokopedia.search.result.domain.model

import com.google.gson.annotations.SerializedName

data class SearchRedeemCouponModel(
    @SerializedName("hachikoRedeem") var hachikoRedeem: HachikoRedeem? = null
) {
    data class Data(
        @SerializedName("hachikoRedeem") var hachikoRedeem: HachikoRedeem? = null
    )

    class HachikoRedeem(
        @SerializedName("coupons") var coupons: ArrayList<Coupons>? = null,
        @SerializedName("reward_points") var rewardPoints: Long? = null,
        @SerializedName("redeemMessage") var redeemMessage: String? = null,
        @SerializedName("ctaList") var ctaList: ArrayList<Cta> = arrayListOf()
    )

    data class Coupons(
        @SerializedName("id") var id: Long? = null,
        @SerializedName("owner") var owner: Long? = null,
        @SerializedName("promo_id") var promoId: Long? = null,
        @SerializedName("code") var code: String? = null,
        @SerializedName("title") var title: String? = null,
        @SerializedName("description") var description: String? = null,
        @SerializedName("cta") var cta: String? = null,
        @SerializedName("cta_desktop") var ctaDesktop: String? = null,
        @SerializedName("url") var url: String? = null,
        @SerializedName("appLink") var appLink: String? = null
    )

    data class Errors(

        @SerializedName("message") var message: String? = null,
        @SerializedName("path") var path: ArrayList<String> = arrayListOf(),
        @SerializedName("extensions") var extensions: Extensions? = Extensions()

    )

    data class Extensions(
        @SerializedName("code") var code: Int? = null,
        @SerializedName("developerMessage") var developerMessage: String? = null,
        @SerializedName("timestamp") var timestamp: String? = null
    )
    data class Cta(
        @SerializedName("text") var text: String? = null,
        @SerializedName("type") var type: String? = null,
        @SerializedName("isDisabled") var isDisabled: Boolean? = null,
        @SerializedName("jsonMetadata") var jsonMetadata: String? = null,
        @SerializedName("toasters") var toasters: ArrayList<String> = arrayListOf()
    )
}
