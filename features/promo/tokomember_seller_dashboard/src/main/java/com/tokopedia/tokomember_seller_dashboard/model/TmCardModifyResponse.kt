package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmCardModifyResponse(
	@Expose
	@SerializedName("data")
	val data: MembershipCreateEditCardResponse? = null
)

data class MembershipCreateEditCardResponse(
	@Expose
	@SerializedName("membershipCreateEditCard")
	val membershipCreateEditCard: MembershipCreateEditCard? = null
)

data class ResultStatusCardModify(
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

data class IntoolsCard(
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
	@SerializedName("shopID")
	val shopID: Int? = null,
	@Expose
	@SerializedName("status")
	val status: Int? = null
)

data class MembershipCreateEditCard(
	@Expose
	@SerializedName("resultStatus")
	val resultStatus: ResultStatus? = null,
	@Expose
	@SerializedName("intoolsCard")
	val intoolsCard: IntoolsCard? = null
)
