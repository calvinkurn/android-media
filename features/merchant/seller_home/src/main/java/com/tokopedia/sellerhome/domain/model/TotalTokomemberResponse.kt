package com.tokopedia.sellerhome.domain.model

import com.google.gson.annotations.SerializedName

data class TotalTokomemberResponse(

	@SerializedName("membershipGetSumUserCardMember")
	val membershipGetSumUserCardMember: MembershipGetSumUserCardMember? = MembershipGetSumUserCardMember()
)

data class SumUserCardMember(

	@SerializedName("sumUserCardMember")
	val sumUserCardMember: Long? = 0,

	@SerializedName("sumUserCardMemberStr")
	val sumUserCardMemberStr: String? = "",

	@SerializedName("isShown")
	val isShown: Boolean? = false,
)

data class ResultStatus(

	@SerializedName("reason")
	val reason: String? = "",

	@SerializedName("code")
	val code: String? = "",

	@SerializedName("message")
	val message: List<String>? = emptyList()
)

data class MembershipGetSumUserCardMember(

	@SerializedName("resultStatus")
	val resultStatus: ResultStatus? = ResultStatus(),

	@SerializedName("sumUserCardMember")
	val sumUserCardMember: SumUserCardMember? = SumUserCardMember()
)
