package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.SerializedName

data class TmGetProgramListResponse(

	@SerializedName("data")
	val data: ProgramList? = null
)

data class Actions(

	@SerializedName("buttons")
	val buttons: List<ButtonsItem?>? = null,

	@SerializedName("tripleDots")
	var tripleDots: ArrayList<TripleDotsItem?>? = null
)

data class Analytics(

	@SerializedName("totalIncome")
	val totalIncome: String? = null,

	@SerializedName("totalNewMember")
	val totalNewMember: String? = null,

	@SerializedName("trxCount")
	val trxCount: String? = null
)

data class ButtonsItem(

	@SerializedName("text")
	val text: String? = null,

	@SerializedName("type")
	val type: String? = null
)

data class ProgramList(

	@SerializedName("membershipGetProgramList")
	val membershipGetProgramList: MembershipGetProgramList? = null
)

data class MembershipGetProgramList(

	@SerializedName("resultStatus")
	val resultStatus: ResultStatus? = null,

	@SerializedName("programSellerList")
	val programSellerList: List<ProgramSellerListItem?>? = null,

	@SerializedName("isDisabledCreateProgram")
	val isDisabledCreateProgram: Boolean? = null,

	@SerializedName("dropdownProgramStatus")
	val dropdownProgramStatus: List<DropdownProgramStatusItem?>? = null,

	@SerializedName("dropdownCardStatus")
	val dropdownCardStatus: List<DropdownCardStatusItem?>? = null
)

data class TimeWindow(

	@SerializedName("startTime")
    var startTime: String? = null,

	@SerializedName("id")
	var id: String? = null,

	@SerializedName("endTime")
	val endTime: String? = null,

	@SerializedName("status")
	val status: Int? = null
)

data class DropdownCardStatusItem(

	@SerializedName("text")
	val text: String? = null,

	@SerializedName("value")
	val value: String? = null
)

data class DropdownProgramStatusItem(

	@SerializedName("text")
	val text: String? = null,

	@SerializedName("value")
	val value: String? = null
)

data class ProgramSellerListItem(

	@SerializedName("analytics")
	val analytics: Analytics? = null,

	@SerializedName("timeWindow")
	val timeWindow: TimeWindow? = null,

	@SerializedName("statusStr")
	val statusStr: String? = null,

	@SerializedName("cardID")
	val cardID: Int? = null,

	@SerializedName("name")
	val name: String? = null,

	@SerializedName("id")
	val id: String? = null,

	@SerializedName("actions")
	val actions: Actions? = null,

	@SerializedName("status")
	val status: Int? = null
)

data class TripleDotsItem(

	@SerializedName("text")
	val text: String? = null,

	@SerializedName("type")
	val type: String? = null
)

data class ProgramItem(
    var programSellerListItem: ProgramSellerListItem,
    var layoutType: LayoutType = LayoutType.SHOW_CARD
)
