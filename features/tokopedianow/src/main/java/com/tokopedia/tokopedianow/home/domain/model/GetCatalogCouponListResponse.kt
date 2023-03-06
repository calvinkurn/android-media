package com.tokopedia.tokopedianow.home.domain.model

import com.google.gson.annotations.SerializedName

data class GetCatalogCouponListResponse(
    @SerializedName("tokopointsCatalogWithCouponList")
    val tokopointsCatalogWithCouponList: TokopointsCatalogWithCouponList
) {
    data class TokopointsCatalogWithCouponList(
        @SerializedName("catalogWithCouponList")
        val catalogWithCouponList: List<CatalogWithCoupon>,
        @SerializedName("resultStatus")
        val resultStatus: ResultStatus
    ) {
        data class CatalogWithCoupon(
            @SerializedName("appLink")
            val appLink: String = "",
            @SerializedName("buttonStr")
            val buttonStr: String = "",
            @SerializedName("id")
            val id: String = "",
            @SerializedName("imageUrlMobile")
            val imageUrlMobile: String = "",
            @SerializedName("slug")
            val slug: String = "",
            @SerializedName("smallImageUrlMobile")
            val smallImageUrlMobile: String = "",
            @SerializedName("title")
            val title: String = ""
        )
        data class ResultStatus(
            @SerializedName("code")
            val code: String = "",
            @SerializedName("message")
            val message: List<String> = listOf(),
            @SerializedName("status")
            val status: String = ""
        )
    }
}
