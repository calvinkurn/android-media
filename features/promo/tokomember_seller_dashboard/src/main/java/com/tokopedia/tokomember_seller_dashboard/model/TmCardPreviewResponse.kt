package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmCardPreviewResponse(
	@Expose
	@SerializedName("data")
	val data: PreviewData? = null
)

data class PreviewData(
	@Expose
	@SerializedName("membershipGetCardList")
	val membershipGetCardList: MembershipGetCardList? = null
)

data class IntoolsCardListItem(
	@Expose
	@SerializedName("tierGroupID")
	val tierGroupID: Int? = null,
	@Expose
	@SerializedName("name")
	val name: String? = null,
	@Expose
	@SerializedName("id")
	val id: Int? = null,
	@Expose
	@SerializedName("status")
	val status: Int? = null
)

data class ResultStatusPreview(
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

data class MembershipGetCardList(
	@Expose
	@SerializedName("resultStatus")
	val resultStatus: ResultStatusPreview? = null,
	@Expose
	@SerializedName("intoolsCardList")
	val intoolsCardList: List<IntoolsCardListItem?>? = null
)
