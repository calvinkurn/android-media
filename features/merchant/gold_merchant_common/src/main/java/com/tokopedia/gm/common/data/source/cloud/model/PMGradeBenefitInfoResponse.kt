package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 10/03/21
 */

data class PMGradeBenefitInfoResponse(
        @SerializedName("goldGetPMGradeBenefitInfo")
        val data: PMGradeBenefitInfoModel? = null
)

data class PMGradeBenefitInfoModel(
        @SerializedName("shop_id")
        val shopId: Long? = 0,
        @SerializedName("next_monthly_refresh_date")
        val nextMonthlyRefreshDate: String? = "",
        @SerializedName("next_quarterly_calibration_refresh_date")
        val nextQuarterlyCalibrationRefreshDate: String? = "",
        @SerializedName("current_pm_grade")
        val currentPMGrade: CurrentPmGradeModel? = null,
        @SerializedName("current_benefit_list")
        val currentPMBenefits: List<PMGradeBenefitModel>? = null,
        @SerializedName("next_pm_grade")
        val nextPMGrade: NextPMGradeModel? = null,
        @SerializedName("next_benefit_list")
        val nextPMBenefits: List<PMGradeBenefitModel>? = null,
        @SerializedName("potential_pm_grade")
        val potentialPmGrade: PotentialPmGradeModel? = null,
        @SerializedName("potential_benefit_list")
        val potentialBenefits: List<PMGradeBenefitModel>? = null,
        @SerializedName("pm_grade_benefit_list")
        val pmGradeBenefitList: List<PmGradeWithBenefitsModel>? = null
)

data class PmGradeWithBenefitsModel(
        @SerializedName("pm_grade_name")
        val gradeName: String? = "",
        @SerializedName("pm_tier")
        val pmTier: Int? = 0,
        @SerializedName("is_active")
        val isActive: Boolean? = false,
        @SerializedName("benefit_list")
        val benefits: List<PMGradeBenefitModel>? = null
)

data class PotentialPmGradeModel(
        @SerializedName("shop_level_current")
        val shopLevelCurrent: Int? = 0,
        @SerializedName("shop_score_current")
        val shopScoreCurrent: Int? = 0,
        @SerializedName("grade_name")
        val gradeName: String? = "",
        @SerializedName("image_badge_url")
        val imgBadgeUrl: String? = "",
        @SerializedName("image_badge_background_mobile_url")
        val backgroundUrl: String? = ""
)

data class NextPMGradeModel(
        @SerializedName("shop_level")
        val shopLevel: Int? = 0,
        @SerializedName("shop_score_min")
        val shopScoreMin: Int? = 0,
        @SerializedName("shop_score_max")
        val shopScoreMax: Int? = 0,
        @SerializedName("grade_name")
        val gradeName: String? = "",
        @SerializedName("image_badge_url")
        val imgBadgeUrl: String? = "",
        @SerializedName("image_badge_background_mobile_url")
        val backgroundUrl: String? = ""
)

data class CurrentPmGradeModel(
        @SerializedName("shop_level")
        val shopLevel: Int? = 0,
        @SerializedName("shop_score")
        val shopScore: Int? = 0,
        @SerializedName("grade_name")
        val gradeName: String? = "",
        @SerializedName("image_badge_url")
        val imgBadgeUrl: String? = "",
        @SerializedName("image_badge_background_mobile_url")
        val backgroundUrl: String? = ""
)

data class PMGradeBenefitModel(
        @SerializedName("benefit_category")
        val categoryName: String? = "",
        @SerializedName("benefit_name")
        val benefitName: String? = "",
        @SerializedName("related_link_applink")
        val appLink: String? = "",
        @SerializedName("seq_num")
        val sequenceNum: Int? = 0,
        @SerializedName("image_url")
        val iconUrl: String? = "",
)