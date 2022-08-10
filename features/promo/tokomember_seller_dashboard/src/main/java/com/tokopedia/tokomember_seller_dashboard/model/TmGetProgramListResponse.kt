package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmGetProgramListResponse(

    @Expose
    @SerializedName("data")
    val data: ProgramList? = null
)

data class Actions(


    @Expose
    @SerializedName("buttons")
    val buttons: List<ButtonsItem?>? = null,


    @Expose
    @SerializedName("tripleDots")
    var tripleDots: ArrayList<TripleDotsItem?>? = null
)

data class Analytics(


    @Expose
    @SerializedName("totalIncome")
    val totalIncome: String = "",


    @Expose
    @SerializedName("totalNewMember")
    val totalNewMember: String = "",


    @Expose
    @SerializedName("trxCount")
    val trxCount: String = ""
)

data class ButtonsItem(


    @Expose
    @SerializedName("text")
    val text: String = "",


    @Expose
    @SerializedName("type")
    val type: String = ""
)

data class ProgramList(


    @Expose
    @SerializedName("membershipGetProgramList")
    val membershipGetProgramList: MembershipGetProgramList? = null
)

data class MembershipGetProgramList(


    @Expose
    @SerializedName("resultStatus")
    val resultStatus: ResultStatus? = null,


    @Expose
    @SerializedName("programSellerList")
    val programSellerList: List<ProgramSellerListItem?>? = null,


    @Expose
    @SerializedName("isDisabledCreateProgram")
    val isDisabledCreateProgram: Boolean = false,


    @Expose
    @SerializedName("dropdownProgramStatus")
    val dropdownProgramStatus: List<DropdownProgramStatusItem?>? = null,


    @Expose
    @SerializedName("dropdownCardStatus")
    val dropdownCardStatus: List<DropdownCardStatusItem?>? = null
)

data class TimeWindow(


    @Expose
    @SerializedName("startTime")
    var startTime: String = "",


    @Expose
    @SerializedName("id")
    var id: String = "",


    @Expose
    @SerializedName("endTime")
    val endTime: String = "",


    @Expose
    @SerializedName("status")
    val status: Int = 0
)

data class DropdownCardStatusItem(


    @Expose
    @SerializedName("text")
    val text: String = "",


    @Expose
    @SerializedName("value")
    val value: String = ""
)

data class DropdownProgramStatusItem(


    @Expose
    @SerializedName("text")
    val text: String = "",


    @Expose
    @SerializedName("value")
    val value: String = ""
)

data class ProgramSellerListItem(


    @Expose
    @SerializedName("analytics")
    val analytics: Analytics? = null,


    @Expose
    @SerializedName("timeWindow")
    val timeWindow: TimeWindow? = null,


    @Expose
    @SerializedName("statusStr")
    val statusStr: String = "",


    @Expose
    @SerializedName("cardID")
    val cardID: Int = 0,


    @Expose
    @SerializedName("name")
    val name: String = "",


    @Expose
    @SerializedName("id")
    val id: String = "",


    @Expose
    @SerializedName("actions")
    val actions: Actions? = null,


    @Expose
    @SerializedName("status")
    val status: Int = 0
)

data class TripleDotsItem(


    @Expose
    @SerializedName("text")
    val text: String = "",


    @Expose
    @SerializedName("type")
    val type: String = ""
)
