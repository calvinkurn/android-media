package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 10/03/21
 */

data class PMGradeBenefitInfoResponse(
        @SerializedName("goldGetPMGradeBenefitInfo")
        @Expose
        val data: PMGradeBenefitInfoModel? = null
)

data class PMGradeBenefitInfoModel(
        @SerializedName("next_monthly_refresh_date")
        @Expose
        val nextMonthlyRefreshDate: String? = "",
        @SerializedName("next_quarterly_calibration_refresh_date")
        @Expose
        val nextQuarterlyCalibrationRefreshDate: String? = "",
        @SerializedName("current_pm_grade")
        @Expose
        val currentPMGrade: CurrentPmGradeModel? = null,
        @SerializedName("current_benefit_list")
        @Expose
        val currentPMBenefits: List<PMGradeBenefitModel>? = null,
        @SerializedName("next_pm_grade")
        @Expose
        val nextPMGrade: NextPMGradeModel? = null,
        @SerializedName("next_benefit_list")
        @Expose
        val nextPMBenefits: List<PMGradeBenefitModel>? = null,
        @SerializedName("pm_grade_benefit_list")
        @Expose
        val pmGradeBenefitList: List<PmGradeWithBenefitsModel>? = null
)

data class PmGradeWithBenefitsModel(
        @SerializedName("pm_grade_name")
        @Expose
        val gradeName: String? = "",
        @SerializedName("pm_tier")
        @Expose
        val pmTier: Int? = 0,
        @SerializedName("is_active")
        @Expose
        val isActive: Boolean? = false,
        @SerializedName("benefit_list")
        @Expose
        val benefits: List<PMGradeBenefitModel>? = null
)

data class NextPMGradeModel(
        @SerializedName("shop_level")
        @Expose
        val shopLevel: Int? = 0,
        @SerializedName("shop_score_min")
        @Expose
        val shopScoreMin: Int? = 0,
        @SerializedName("shop_score_max")
        @Expose
        val shopScoreMax: Int? = 0,
        @SerializedName("grade_name")
        @Expose
        val gradeName: String? = "",
        @SerializedName("image_badge_url")
        @Expose
        val imgBadgeUrl: String? = "",
        @SerializedName("image_badge_background_mobile_url")
        @Expose
        val backgroundUrl: String? = ""
)

data class CurrentPmGradeModel(
        @SerializedName("grade_name")
        @Expose
        val gradeName: String? = "",
        @SerializedName("image_badge_url")
        @Expose
        val imgBadgeUrl: String? = "",
        @SerializedName("image_badge_background_mobile_url")
        @Expose
        val backgroundUrl: String? = "",
        @Expose
        @SerializedName("shop_level")
        val shopLevel: String? = ""
)

data class PMGradeBenefitModel(
        @SerializedName("benefit_category")
        @Expose
        val categoryName: String? = "",
        @SerializedName("benefit_name")
        @Expose
        val benefitName: String? = "",
        @SerializedName("related_link_applink")
        @Expose
        val appLink: String? = "",
        @SerializedName("seq_num")
        @Expose
        val sequenceNum: Int? = 0,
        @SerializedName("image_url")
        @Expose
        val iconUrl: String? = "",
)