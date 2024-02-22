package com.tokopedia.gamification.pdp.data.model

import com.google.gson.annotations.SerializedName

data class KetupatBenefitCouponData(
    @SerializedName("tokopointsCouponList")
    var tokopointsCouponList: TokopointsCouponList
) {
    data class TokopointsCouponList(
        @SerializedName("tokopointsCouponData")
        var tokopointsCouponData: List<TokopointsCouponData?>,
        @SerializedName("tokopointsExtraInfo")
        var tokopointsExtraInfo: List<Any?>,
        @SerializedName("tokopointsPaging")
        var tokopointsPaging: TokopointsPaging?,
        @SerializedName("tokopointsEmptyMessage")
        var tokopointsEmptyMessage: TokopointsEmptyMessage?
    ) {
        data class TokopointsCouponData(
            @SerializedName("id")
            val id: Long,
            @SerializedName("code")
            val code: String?,
            @SerializedName("imageUrlMobile")
            val imageUrlMobile: String?
        )

        data class TokopointsExtraInfo(
            @SerializedName("linkText")
            val linkText: String?,
            @SerializedName("infoHTML")
            val infoHTML: String?,
            @SerializedName("linkUrl")
            val linkUrl: String?
        )

        data class TokopointsPaging(
            @SerializedName("hasNext")
            val hasNext: Boolean
        )

        data class TokopointsEmptyMessage(
            @SerializedName("title")
            val title: String?,
            @SerializedName("subTitle")
            val subTitle: String?
        )
    }
}
