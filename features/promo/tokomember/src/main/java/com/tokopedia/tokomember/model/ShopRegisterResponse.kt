package com.tokopedia.tokomember.model

import com.google.gson.annotations.SerializedName

data class ShopRegisterResponse(

	@SerializedName("data")
	val data: Data? = null
)

data class BottomSheetContentItem(

	@SerializedName("cta")
	val cta: CtaShopRegister? = null,

	@SerializedName("imageURL")
	val imageURL: String? = null,

	@SerializedName("description")
	val description: String? = null,

	@SerializedName("title")
	val title: String? = null
)

data class CtaShopRegister(

	@SerializedName("appLink")
	val appLink: String? = null,

	@SerializedName("isShown")
	val isShown: Boolean? = null,

	@SerializedName("text")
	val text: String? = null,

	@SerializedName("url")
	val url: String? = null
)

data class Data(

	@SerializedName("membershipGetShopRegistrationWidget")
	val membershipGetShopRegistrationWidget: MembershipGetShopRegistrationWidget? = null
)

data class MembershipGetShopRegistrationWidget(

	@SerializedName("resultStatus")
	val resultStatus: ResultStatusShopRegister? = null,

	@SerializedName("membershipType")
	val membershipType: Int? = null,

	@SerializedName("cardID")
	val cardID: Int? = null,

	@SerializedName("widgetContent")
	val widgetContent: List<WidgetContentItem?>? = null,

	@SerializedName("bottomSheetContent")
	val bottomSheetContent: List<BottomSheetContentItem?>? = null
)

data class WidgetContentItem(

	@SerializedName("appLink")
	val appLink: String? = null,

	@SerializedName("usecase")
	val usecase: String? = null,

	@SerializedName("isOpenBottomSheet")
	val isOpenBottomSheet: Boolean? = null,

	@SerializedName("imageURL")
	val imageURL: String? = null,

	@SerializedName("description")
	val description: String? = null,

	@SerializedName("isShown")
	val isShown: Boolean? = null,

	@SerializedName("title")
	val title: String? = null,

	@SerializedName("url")
	val url: String? = null
)

data class ResultStatusShopRegister(

	@SerializedName("reason")
	val reason: String? = null,

	@SerializedName("code")
	val code: String? = null,

	@SerializedName("message")
	val message: List<String?>? = null
)

