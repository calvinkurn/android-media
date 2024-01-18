package com.tokopedia.gamification.pdp.data.model

import com.google.gson.annotations.SerializedName

data class KetupatBenefitCouponSlugData(
    @SerializedName("tokopointsCouponListStack")
    var tokopointsCouponListStack: TokopointsCouponListStack
) {
    data class TokopointsCouponListStack(
        @SerializedName("tokopointsCouponDataStack")
        var tokopointsCouponDataStack: List<TokopointsCouponDataStack?>,
        @SerializedName("tokopointsExtraInfo")
        var tokopointsExtraInfo: List<TokopointsExtraInfo?>,
        @SerializedName("tokopointsPaging")
        var tokopointsPaging: TokopointsPaging?,
        @SerializedName("tokopointsEmptyMessage")
        var tokopointsEmptyMessage: TokopointsEmptyMessage?
    ) {
        data class TokopointsCouponDataStack(
            @SerializedName("id")
            val id: Long,
            @SerializedName("code")
            val code: String?,
            @SerializedName("imageURLMobile")
            val imageURLMobile: String?
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
