package com.tokopedia.power_merchant.subscribe.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.gm.common.data.source.cloud.model.ShopScoreLevelModel

data class MembershipDetailResponse(
    @Expose
    @SerializedName("shopLevel")
    val shopLevel: ShopLevel = ShopLevel(),
    @Expose
    @SerializedName("goldGetPMGradeBenefitInfo")
    val data: GoldGetPMGradeBenefitInfo? = null,
    @Expose
    @SerializedName("goldGetPMShopInfo")
    val pmShopInfo: PMShopInfoModel? = null,
    @Expose
    @SerializedName("shopScoreLevel")
    val shopScoreInfo: ShopScoreLevelModel? = null
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
            val period: String? = "",
            @Expose
            @SerializedName("itemSold")
            val itemSold: Double? = 0.0,
            @Expose
            @SerializedName("niv")
            val netIncomeValue: Double? = 0.0
        )
    }

    data class GoldGetPMGradeBenefitInfo(
        @Expose
        @SerializedName("next_monthly_refresh_date")
        val nextMonthlyRefreshDate: String? = "",
        @Expose
        @SerializedName("next_quarterly_calibration_refresh_date")
        val nextQuarterlyCalibrationRefreshDate: String? = "",
        @Expose
        @SerializedName("current_pm_grade")
        val currentPMGrade: CurrentPmGradeModel? = null
    )

    data class CurrentPmGradeModel(
        @Expose
        @SerializedName("grade_name")
        val gradeName: String? = ""
    )

    data class PMShopInfoModel(
        @SerializedName("item_sold_one_month")
        @Expose
        val itemSold: Long = 0,
        @SerializedName("niv_one_month")
        @Expose
        val netIncome: Long = 0
    )
}