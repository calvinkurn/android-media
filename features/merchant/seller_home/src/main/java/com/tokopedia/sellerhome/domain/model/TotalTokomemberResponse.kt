package com.tokopedia.sellerhome.domain.model

import com.google.gson.annotations.SerializedName

data class TotalTokomemberResponse(

	@SerializedName("membershipGetSumUserCardMember")
	val membershipGetSumUserCardMember: MembershipGetSumUserCardMember? = null
)

data class SumUserCardMember(

	@SerializedName("sumUserCardMember")
	val sumUserCardMember: Long? = 0L,

	@SerializedName("sumUserCardMemberStr")
	val sumUserCardMemberStr: String? = "0",

	@SerializedName("isShown")
	val isShown: Boolean? = false,
)

data class ResultStatus(

	@SerializedName("reason")
	val reason: String? = null,

	@SerializedName("code")
	val code: String? = null,

	@SerializedName("message")
	val message: List<String?>? = null
)

data class MembershipGetSumUserCardMember(

	@SerializedName("resultStatus")
	val resultStatus: ResultStatus? = null,

	@SerializedName("sumUserCardMember")
	val sumUserCardMember: SumUserCardMember? = null
)
