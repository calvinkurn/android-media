package com.tokopedia.tokopoints.view.model.usersaving

import com.google.gson.annotations.SerializedName

data class UserSavingResponse(

	@SerializedName("tokopointsUserSaving")
	val tokopointsUserSaving: TokopointsUserSaving? = null
)

data class ResultStatus(

	@SerializedName("code")
	val code: String? = null,

	@SerializedName("status")
	val status: String? = null
)

data class TokopointsUserSaving(

	@SerializedName("resultStatus")
	val resultStatus: ResultStatus? = null,

	@SerializedName("userSavingInfo")
	val userSavingInfo: UserSavingInfo? = null,

	@SerializedName("userSaving")
	val userSaving: UserSaving? = null,

	@SerializedName("userSavingDetailList")
	val userSavingDetailList: List<UserSavingDetailListItem?>? = null
)

data class UserSaving(

	@SerializedName("backgroundImageURLOptimized")
	val backgroundImageURLOptimized: String? = null,

	@SerializedName("backgroundImageURLDesktop")
	val backgroundImageURLDesktop: String? = null,

	@SerializedName("title")
	val title: String? = null,

	@SerializedName("userTotalSaving")
	val userTotalSaving: Int? = null,

	@SerializedName("descriptions")
	val descriptions: List<DescriptionsItem?>? = null,

	@SerializedName("userTotalSavingStr")
	val userTotalSavingStr: String? = null,

	@SerializedName("backgroundImageURL")
	val backgroundImageURL: String? = null
)

data class UserSavingDetailListItem(

	@SerializedName("amountUserSavingDetailStr")
	val amountUserSavingDetailStr: String? = null,

	@SerializedName("iconImageURL")
	val iconImageURL: String? = null,

	@SerializedName("iconImageURLOptimized")
	val iconImageURLOptimized: String? = null,

	@SerializedName("amountUserSavingDetail")
	val amountUserSavingDetail: Int? = null,

	@SerializedName("title")
	val title: String? = null
)

data class UserSavingInfo(

	@SerializedName("bottomSheetContentList")
	val bottomSheetContentList: List<BottomSheetContentListItem?>? = null,

	@SerializedName("title")
	val title: String? = null
)

data class DescriptionsItem(

	@SerializedName("text")
	val text: String? = null,

	@SerializedName("fontStyle")
	val fontStyle: String? = null
)

data class BottomSheetContentListItem(

	@SerializedName("number")
	val number: String? = null,

	@SerializedName("description")
	val description: String? = null
)
