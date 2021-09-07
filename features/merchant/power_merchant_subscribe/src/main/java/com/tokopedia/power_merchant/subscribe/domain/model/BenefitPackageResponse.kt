package com.tokopedia.power_merchant.subscribe.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.gm.common.constant.PMTier
import com.tokopedia.gm.common.data.source.cloud.model.PMGradeBenefitInfoModel

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
        val currentPMGrade: CurrentPmGradeModel? = null,
        @Expose
        @SerializedName("next_level_benefit_package_list")
        val nextBenefitPackageList: List<NextBenefitPackageList>? = emptyList()
    )

    data class NextBenefitPackageList(
        @Expose
        @SerializedName("pm_grade_name")
        val pmGradeName: String = "",
        @Expose
        @SerializedName("image_badge_url")
        val imageBadgeUrl: String = "",
        @Expose
        @SerializedName("is_active")
        val isActive: Boolean = false,
        @Expose
        @SerializedName("pm_tier")
        val pmTier: Int = PMTier.REGULAR,
        @Expose
        @SerializedName("benefit_list")
        val benefitList: List<PMBenefitData> = listOf()
    )

    data class PMBenefitData(
        @Expose
        @SerializedName("benefit_category")
        val benefitCategory: String = "",
        @Expose
        @SerializedName("benefit_name")
        val benefitName: String = "",
        @Expose
        @SerializedName("benefit_description")
        val benefitDescription: String = "",
        @Expose
        @SerializedName("related_link_applink")
        val relatedLinkApplink: String = "",
        @Expose
        @SerializedName("related_link_name")
        val relatedLinkName: String = "",
        @Expose
        @SerializedName("seq_num")
        val seqNum: Int = 0,
        @Expose
        @SerializedName("image_url")
        val imageUrl: String = ""
    )

    data class CurrentPmGradeModel(
        @Expose
        @SerializedName("grade_name")
        val gradeName: String? = ""
    )
}