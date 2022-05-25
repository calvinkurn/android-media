package com.tokopedia.power_merchant.subscribe.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BenefitPackageResponse(
    @Expose
    @SerializedName("shopLevel")
    val shopLevel: ShopLevel = ShopLevel(),
    @Expose
    @SerializedName("goldGetPMGradeBenefitInfo")
    val data: GoldGetPMGradeBenefitInfo? = null
) {
    data class ShopLevel(
        @Expose
        @SerializedName("error")
        val error: Error = Error(),
        @Expose
        @SerializedName("result")
        val result: Result = Result()
    ) {
        data class Error(
            @Expose
            @SerializedName("message")
            val message: String = ""
        )

        data class Result(
            @Expose
            @SerializedName("period")
            val period: String? = ""
        )
    }

    data class GoldGetPMGradeBenefitInfo(
        @Expose
        @SerializedName("next_monthly_refresh_date")
        val nextMonthlyRefreshDate: String? = "",
        @Expose
        @SerializedName("current_pm_grade")
        val currentPMGrade: CurrentPmGradeModel? = null
    )

    data class CurrentPmGradeModel(
        @Expose
        @SerializedName("grade_name")
        val gradeName: String? = ""
    )
}