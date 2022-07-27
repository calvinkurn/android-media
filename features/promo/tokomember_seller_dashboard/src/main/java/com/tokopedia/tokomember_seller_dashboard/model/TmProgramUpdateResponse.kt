package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmProgramUpdateResponse(

	@Expose
	@SerializedName("data")
	val data: ProgramUpdateResponse? = null
)

data class MembershipCreateEditProgram(
	@Expose
	@SerializedName("resultStatus")
	val resultStatus: ResultStatusProgramUpdate? = null,
	@Expose
	@SerializedName("programSeller")
	val programSeller: ProgramSeller? = null
)

data class TierLevelsItemProgramUpdate(
	@Expose
	@SerializedName("tierGroupID")
	val tierGroupID: Int? = null,
	@Expose
	@SerializedName("level")
	val level: Int? = null,
	@Expose
	@SerializedName("name")
	val name: String? = null,
	@Expose
	@SerializedName("threshold")
	val threshold: Int? = null,
	@Expose
	@SerializedName("id")
	val id: Int? = null
)

data class ProgramUpdateResponse(
	@Expose
	@SerializedName("membershipCreateEditProgram")
	val membershipCreateEditProgram: MembershipCreateEditProgram? = null
)

data class ResultStatusProgramUpdate(
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

data class ProgramAttributesItem(
	@Expose
	@SerializedName("minimumTransaction")
	val minimumTransaction: Int? = null,
	@Expose
	@SerializedName("multiplierRates")
	val multiplierRates: Int? = null,
	@Expose
	@SerializedName("tierLevelID")
	val tierLevelID: Int? = null,
	@Expose
	@SerializedName("id")
	val id: Int? = null,
	@Expose
	@SerializedName("isUseMultiplier")
	val isUseMultiplier: Boolean? = null,
	@Expose
	@SerializedName("programID")
	val programID: Int? = null
)

data class ProgramTimeWindow(
	@Expose
	@SerializedName("startTime")
	val startTime: String? = null,
	@Expose
	@SerializedName("id")
	val id: Int? = null,
	@Expose
	@SerializedName("endTime")
	val endTime: String? = null,
	@Expose
	@SerializedName("referenceID")
	val referenceID: Int? = null
)

data class ProgramSeller(
	@Expose
	@SerializedName("timeWindow")
	val timeWindow: ProgramTimeWindow? = null,
	@Expose
	@SerializedName("programAttributes")
	val programAttributes: List<ProgramAttributesItem?>? = null,
	@Expose
	@SerializedName("cardID")
	val cardID: Int? = null,
	@Expose
	@SerializedName("name")
	val name: String? = null,
	@Expose
	@SerializedName("id")
	val id: Int? = null,
	@Expose
	@SerializedName("tierLevels")
	val tierLevels: List<TierLevelsItemProgramUpdate?>? = null
)
