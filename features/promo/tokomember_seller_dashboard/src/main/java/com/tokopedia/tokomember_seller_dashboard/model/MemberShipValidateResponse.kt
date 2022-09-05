package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MemberShipValidateResponse(
	@Expose
	@SerializedName("membershipValidateBenefit")
	val membershipValidateBenefit: MembershipValidateBenefit? = null
)

data class ResultStatusMembershipValidate(
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

data class MembershipValidateBenefit(
	@Expose
	@SerializedName("resultStatus")
	val resultStatus: ResultStatusMembershipValidate? = null,
	@Expose
	@SerializedName("isValidMembershipBenefit")
	val isValidMembershipBenefit: Boolean? = null
)
