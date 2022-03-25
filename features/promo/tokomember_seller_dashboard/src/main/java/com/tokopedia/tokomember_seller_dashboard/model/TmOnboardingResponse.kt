package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmOnboardingResponse(
    @Expose
    @SerializedName("data")
    val data: Data? = null
)

data class SellerHomeInfo(
    @Expose
    @SerializedName("infoURL")
    val infoURL: String? = null,
    @Expose
    @SerializedName("type")
    val type: String? = null
)

data class SellerHomeText(
    @Expose
    @SerializedName("subTitle")
    val subTitle: List<String?>? = null,
    @Expose
    @SerializedName("sellerHomeTextBenefit")
    val sellerHomeTextBenefit: List<SellerHomeTextBenefitItem?>? = null,
    @Expose
    @SerializedName("title")
    val title: List<String?>? = null
)

data class ResultStatus(
    @Expose
    @SerializedName("reason")
    val reason: String? = null,
    @Expose
    @SerializedName("code")
    val code: String? = null,
    @Expose
    @SerializedName("message")
    val message: List<String?>? = null
)

data class CTAItem(
    @Expose
    @SerializedName("text")
    val text: String? = null
)

data class SellerHomeTextBenefitItem(
    @Expose
    @SerializedName("iconURL")
    val iconURL: String? = null,
    @Expose
    @SerializedName("benefit")
    val benefit: String? = null
)

data class CatalogsItem(
    @Expose
    @SerializedName("level")
    val level: Int? = null,
    @Expose
    @SerializedName("isHasCatalog")
    val isHasCatalog: Boolean? = null
)

data class SellerHomeContent(
    @Expose
    @SerializedName("CTA")
    val cTA: List<CTAItem?>? = null,
    @Expose
    @SerializedName("sellerHomeInfo")
    val sellerHomeInfo: SellerHomeInfo? = null,
    @Expose
    @SerializedName("sellerHomeText")
    val sellerHomeText: SellerHomeText? = null,
    @Expose
    @SerializedName("isShowContent")
    val isShowContent: Boolean? = null
)

data class MembershipGetSellerOnboarding(
    @Expose
    @SerializedName("resultStatus")
    val resultStatus: ResultStatus? = null,
    @Expose
    @SerializedName("isHasCard")
    val isHasCard: Boolean? = null,
    @Expose
    @SerializedName("sellerHomeContent")
    val sellerHomeContent: SellerHomeContent? = null,
    @Expose
    @SerializedName("catalogs")
    val catalogs: List<CatalogsItem?>? = null,
    @Expose
    @SerializedName("cardID")
    val cardID: String? = null,
    @Expose
    @SerializedName("isHasCatalog")
    val isHasCatalog: Boolean? = null,
    @Expose
    @SerializedName("isHasProgram")
    val isHasProgram: Boolean? = null,
    @Expose
    @SerializedName("isHasActiveProgram")
    val isHasActiveProgram: Boolean? = null
)

data class Data(
    @Expose
    @SerializedName("membershipGetSellerOnboarding")
    val membershipGetSellerOnboarding: MembershipGetSellerOnboarding? = null
)
