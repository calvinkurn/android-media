package com.tokopedia.vouchercreation.shop.create.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.network.data.model.response.Header

data class InitiateVoucherResponse(
        @SerializedName("getInitiateVoucherPage")
        val initiateVoucherPage: InitiateVoucherPage = InitiateVoucherPage()
)

data class InitiateVoucherPage(
        @SerializedName("header")
        val header: Header = Header(),
        @SerializedName("data")
        val initiateVoucherPageData: InitiateVoucherPageData = InitiateVoucherPageData()
)

data class InitiateVoucherPageData(
        @SerializedName("shop_id")
        val shopId: Int = 0,
        @SerializedName("token")
        val token: String = "",
        @SerializedName("user_id")
        val userId: Int = 0,
        @SerializedName("access_token")
        val accessToken: String = "",
        @SerializedName("upload_app_url")
        val uploadAppUrl: String = "",
        @SerializedName("img_banner_base")
        val bannerBaseUrl: String = "",
        @SerializedName("img_banner_ig_post")
        val bannerIgPostUrl: String = "",
        @SerializedName("img_banner_ig_story")
        val bannerIgStoryUrl: String = "",
        @SerializedName("img_banner_label_gratis_ongkir")
        val bannerFreeShippingLabelUrl: String = "",
        @SerializedName("img_banner_label_cashback")
        val bannerCashbackLabelUrl: String = "",
        @SerializedName("img_banner_label_cashback_hingga")
        val bannerCashbackUntilLabelUrl: String = "",
        @SerializedName("prefix_voucher_code")
        val voucherCodePrefix: String = "",
        @SerializedName("is_eligible")
        val isEligible: Int = 0,
        @SerializedName("max_product")
        val maxProduct: Int = 0
)