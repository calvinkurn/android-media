package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmGetProgramResponse(
	@Expose
	@SerializedName("data")
	val data: ProgramFormData? = null
)

data class TierLevelsItem(
	@Expose
	@SerializedName("tierGroupID")
	val tierGroupID: Int? = null
)

data class ProgramForm(
	@Expose
	@SerializedName("tierLevels")
	val tierLevels: List<TierLevelsItem?>? = null
)

data class MembershipGetProgramForm(
	@Expose
	@SerializedName("resultStatus")
	val resultStatus: ResultStatusProgramForm? = null,
	@Expose
	@SerializedName("programForm")
	val programForm: ProgramForm? = null,
	@Expose
	@SerializedName("timePeriodList")
	val timePeriodList: List<TimePeriodListItem?>? = null
)

data class ResultStatusProgramForm(
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

data class TimePeriodListItem(
	@Expose
	@SerializedName("months")
	val months: Int? = null,
	@Expose
	@SerializedName("name")
	val name: String? = null
)

data class ProgramFormData(
	@Expose
	@SerializedName("membershipGetProgramForm")
	val membershipGetProgramForm: MembershipGetProgramForm? = null
)
