package com.tokopedia.tokomember.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class MembershipShopResponse(

	@SerializedName("membershipGetShopRegistrationWidget")
	val membershipGetShopRegistrationWidget: MembershipGetShopRegistrationWidget? = null
)

@Parcelize
data class BottomSheetContentItem (

	@SerializedName("cta")
	val cta: CtaShopRegister? = CtaShopRegister() ,

	@SerializedName("imageURL")
	val imageURL: String? = "",

	@SerializedName("description")
	val description: String? = "",

	@SerializedName("title")
	val title: String? = "" ,

	@SerializedName("source")
	var source: Int? = 0 ,

	@SerializedName("shopID")
	var shopID: Int? = 0 ,

	@SerializedName("membershipType")
	var membershipType: Int? = 0 ,

	@SerializedName("paymentID")
	var paymentID: String? = ""
	) : Parcelable

@Parcelize
data class CtaShopRegister(

	@SerializedName("appLink")
	val appLink: String? = "",

	@SerializedName("isShown")
	val isShown: Boolean? = false,

	@SerializedName("text")
	val text: String? = "",

	@SerializedName("url")
	val url: String? = ""
) : Parcelable

data class Data(

	@SerializedName("membershipGetShopRegistrationWidget")
	val membershipGetShopRegistrationWidget: MembershipGetShopRegistrationWidget? = null
)

@Parcelize
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
) : Parcelable

@Parcelize
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
) : Parcelable

@Parcelize
data class ResultStatusShopRegister(

	@SerializedName("reason")
	val reason: String? = null,

	@SerializedName("code")
	val code: String? = null,

	@SerializedName("message")
	val message: List<String?>? = null
) : Parcelable

@SuppressLint("ResponseFieldAnnotation")
data class MembershipOrderData(
	@SerializedName("shopID")
	var shopID: Int = 0,
	@SerializedName("amount")
	var amount: Float = 0F
)

