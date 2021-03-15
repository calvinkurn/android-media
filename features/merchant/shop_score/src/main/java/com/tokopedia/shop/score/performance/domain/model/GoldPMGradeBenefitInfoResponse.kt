package com.tokopedia.shop.score.performance.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GoldPMGradeBenefitInfoResponse(
        @Expose
        @SerializedName("goldGetPMGradeBenefitInfo")
        val goldGetPMGradeBenefitInfo: GoldGetPMGradeBenefitInfo = GoldGetPMGradeBenefitInfo()
) {
    data class GoldGetPMGradeBenefitInfo(
            @Expose
            @SerializedName("current_pm_grade")
            val currentPmGrade: CurrentPmGrade = CurrentPmGrade(),
            @Expose
            @SerializedName("potential_pm_grade")
            val potentialPmGrade: PotentialPmGrade = PotentialPmGrade()
    ) {
        data class CurrentPmGrade(
                @Expose
                @SerializedName("grade_name")
                val gradeName: String = "",
                @Expose
                @SerializedName("image_badge_background_mobile_url")
                val imageBadgeBackgroundMobileUrl: String = "",
                @Expose
                @SerializedName("image_badge_url")
                val imageBadgeUrl: String = "",
                @Expose
                @SerializedName("shop_level_actual")
                val shopLevelActual: Int = 0,
                @Expose
                @SerializedName("shop_score_actual")
                val shopScoreActual: Int = 0
        )

        data class PotentialPmGrade(
                @Expose
                @SerializedName("grade_name")
                val gradeName: String = "",
                @Expose
                @SerializedName("image_badge_background_mobile_url")
                val imageBadgeBackgroundMobileUrl: String = "",
                @Expose
                @SerializedName("image_badge_url")
                val imageBadgeUrl: String = "",
                @Expose
                @SerializedName("shop_level_current")
                val shopLevelCurrent: Int = 0,
                @Expose
                @SerializedName("shop_score_current")
                val shopScoreCurrent: Int = 0
        )
    }
}