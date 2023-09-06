package com.tokopedia.scp_rewards.detail.domain.model

import com.google.gson.annotations.SerializedName

data class MedalDetailResponseModel(
    @SerializedName("scpRewardsMedaliDetailPage") val detail: RewardsGetMedaliDetail? = null
)

data class RewardsGetMedaliDetail(
    @SerializedName("resultStatus") val resultStatus: ResultStatus = ResultStatus(),
    @SerializedName("medaliDetailPage") val medaliDetailPage: MedaliDetailPage? = null
) {
    data class ResultStatus(
        @SerializedName("code") val code: String = "",
        @SerializedName("status") val status: String = ""
    )
}

data class MedaliDetailPage(
    @SerializedName("backgroundImageURL") val backgroundImageURL: String? = null,
    @SerializedName("backgroundImageColor") val backgroundImageColor: String? = null,
    @SerializedName("frameAltImageURL") val frameImageURL: String? = null,
    @SerializedName("innerIconImageURL") val innerIconImageURL: String? = null,
    @SerializedName("maskingImageURL") val shutterMaskingImageURL: String? = null,
    @SerializedName("frameMaskingImageURL") val frameMaskingImageURL: String? = null,
    @SerializedName("shutterImageURL") val shutterImageURL: String? = null,
    @SerializedName("shutterText") val shutterText: String? = null,
    @SerializedName("shimmerAltImageURL") val shimmerImageURL: String? = null,
    @SerializedName("shimmerShutterLottieURL") val shimmerShutterLottieURL: String? = null,
    @SerializedName("outerBlinkingLottieURL") val outerBlinkingLottieURL: String? = null,
    @SerializedName("baseImageURL") val baseImageURL: String? = null,
    @SerializedName("sourceText") val sourceText: String? = null,
    @SerializedName("sourceFontColor") val sourceFontColor: String? = null,
    @SerializedName("sourceBackgroundColor") val sourceBackgroundColor: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("isMedaliGrayScale") val isMedaliGrayScale: Boolean? = null,
    @SerializedName("iconImageURL") val iconImageURL: String? = null,
    @SerializedName("tncButton") val tncButton: TncButton? = null,
    @SerializedName("coachMark") val coachMark: CoachMark? = null,
    @SerializedName("mission") val mission: Mission? = null,
    @SerializedName("benefit") val benefits: List<Benefit>? = null,
    @SerializedName("section") val section: List<MdpSection>? = null,
    @SerializedName("benefitButton") val benefitButtons: List<BenefitButton>? = null
)

data class TncButton(
    @SerializedName("text") val text: String? = null,
    @SerializedName("url") val url: String? = null,
    @SerializedName("appLink") val appLink: String? = null
)

data class CoachMark(
    @SerializedName("text") val text: String? = null,
    @SerializedName("showNumberOfTimes") val showNumberOfTimes: Int? = null,
    @SerializedName("timeOut") val timeOut: Long? = null
)

data class Mission(
    @SerializedName("title") val title: String? = null,
    @SerializedName("progress") val progress: Int? = null,
    @SerializedName("task") val task: List<Task>? = null
)

data class Task(
    @SerializedName("isCompleted") val isCompleted: Boolean = false,
    @SerializedName("title") val title: String? = null,
    @SerializedName("progressInfo") val progressInfo: String? = null,
)

data class MdpSection(
    @SerializedName("id") val id:  Int? = null,
    @SerializedName("layout") val type: String? = null,
    @SerializedName("medaliSectionTitle") val medaliSectionTitle: MedaliSectionTitle? = null,
    @SerializedName("backgroundColor") val backgroundColor: String? = null,
    @SerializedName("jsonParameter") val jsonParameter: String? = null,
)

data class MedaliSectionTitle(
    @SerializedName("content") val content: String? = null
)

data class Benefit(
    @SerializedName("imageURL") val imageUrl: String? = null,
    @SerializedName("isActive") val isActive: Boolean = false,
    @SerializedName("status") val status: String? = null,
    @SerializedName("statusDescription") val statusDescription: String? = null
)

data class BenefitButton(
    @SerializedName("unifiedStyle") val unifiedStyle: String? = null,
    @SerializedName("text") val text: String? = null,
    @SerializedName("url") val url: String? = null,
    @SerializedName("appLink") val appLink: String? = null,
    @SerializedName("isAutoApply") val isAutoApply: Boolean = false,
    @SerializedName("couponCode") val couponCode: String? = null
)
