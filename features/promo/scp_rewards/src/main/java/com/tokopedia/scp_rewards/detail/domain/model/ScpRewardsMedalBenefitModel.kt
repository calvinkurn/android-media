package com.tokopedia.scp_rewards.detail.domain.model

import com.google.gson.annotations.SerializedName

data class MedalBenefitResponseModel(
    @SerializedName("scpRewardsMedaliBenefitList") val detail: RewardsGetMedaliBenefit? = null
)

data class RewardsGetMedaliBenefit(
    @SerializedName("resultStatus") val resultStatus: ResultStatus = ResultStatus(),
    @SerializedName("medaliBenefitList") val medaliBenefitList: MedaliBenefitList? = null
) {
    data class ResultStatus(
        @SerializedName("code") val code: String = "",
        @SerializedName("status") val status: String = ""
    )
}

data class MedaliBenefitList(
    @SerializedName("category") val categoryList: List<Category>? = null,
    @SerializedName("benefitInfo") val benefitInfo: String? = null,
    @SerializedName("benefit") val benefitList: List<MedaliBenefit>? = null,
    @SerializedName("cta") val cta: Cta? = null,
    @SerializedName("paging") val paging: PageInfo? = null,
)

data class Category(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("text") val text: String? = null,
    @SerializedName("iconImageURL") val iconImageURL: String? = null
)

data class MedaliBenefit(
    @SerializedName("categoryID") val categoryId: Int? = null,
    @SerializedName("backgroundImageURL") val backgroundImageURL: String? = null,
    @SerializedName("podiumImageURL") val podiumImageURL: String? = null,
    @SerializedName("iconImageURL") val iconImageURL: String? = null,
    @SerializedName("ribbonImageURL") val ribbonImageURL: String? = null,
    @SerializedName("dividerImageURL") val dividerImageURL: String? = null,
    @SerializedName("statusImageURL") val statusImageURL: String? = null,
    @SerializedName("isActive") val isActive: Boolean = false,
    @SerializedName("status") val status: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("statusDescription") val statusDescription: String? = null,
    @SerializedName("expiryCounter") val expiryCounter: String? = null,
    @SerializedName("info") val info: String? = null,
    @SerializedName("appLink") val appLink: String? = null,
    @SerializedName("tnc") val tncList: List<Tnc>? = null,
    @SerializedName("benefitCTA") val cta: BenefitButton? = null
)

data class Tnc(
    @SerializedName("text") val text: String? = null
)

data class Cta(
    @SerializedName("unifiedStyle") val unifiedStyle: String? = null,
    @SerializedName("text") val text: String? = null,
    @SerializedName("url") val url: String? = null,
    @SerializedName("appLink") val appLink: String? = null
)

data class PageInfo(
    @SerializedName("hasNext") val hasNext: Boolean? = null
)
