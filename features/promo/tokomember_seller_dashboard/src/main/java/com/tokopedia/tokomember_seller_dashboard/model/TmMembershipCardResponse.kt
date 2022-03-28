package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmMembershipCardResponse(
	@Expose
	@SerializedName("data")
	val data: CardData? = null
)

data class CardTemplateImageListItem(
	@Expose
	@SerializedName("imageURL")
	val imageURL: String? = null,
	@Expose
	@SerializedName("name")
	val name: String? = null,
	@Expose
	@SerializedName("isChoosen")
	val isChoosen: Boolean? = null
)

data class CardData(
	@Expose
	@SerializedName("membershipGetCardForm")
	val membershipGetCardForm: MembershipGetCardForm? = null
)

data class CardResultStatus(
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

data class ColorTemplateListItem(
	@Expose
	@SerializedName("colorCode")
	val colorCode: String? = null,
	@Expose
	@SerializedName("id")
	val id: String? = null
)

data class Card(
	@Expose
	@SerializedName("tierGroupID")
	val tierGroupID: Int? = null,
	@Expose
	@SerializedName("name")
	val name: String? = null,
	@Expose
	@SerializedName("id")
	val id: String? = null,
	@Expose
	@SerializedName("shopID")
	val shopID: Int? = null,
	@Expose
	@SerializedName("numberOfLevel")
	val numberOfLevel: Int? = null,
	@Expose
	@SerializedName("status")
	val status: Int? = null
)

data class CardTemplate(
	@Expose
	@SerializedName("backgroundColor")
	val backgroundColor: String? = null,
	@Expose
	@SerializedName("cardID")
	val cardID: Int? = null,
	@Expose
	@SerializedName("id")
	val id: String? = null,
	@Expose
	@SerializedName("fontColor")
	val fontColor: String? = null,
	@Expose
	@SerializedName("backgroundImgUrl")
	val backgroundImgUrl: String? = null
)

data class MembershipGetCardForm(
	@Expose
	@SerializedName("resultStatus")
	val resultStatus: CardResultStatus? = null,
	@Expose
	@SerializedName("shopAvatar")
	val shopAvatar: String? = null,
	@Expose
	@SerializedName("patternList")
	val patternList: List<String?>? = null,
	@Expose
	@SerializedName("colorTemplateList")
	val colorTemplateList: List<ColorTemplateListItem?>? = null,
	@Expose
	@SerializedName("cardTemplate")
	val cardTemplate: CardTemplate? = null,
	@Expose
	@SerializedName("cardTemplateImageList")
	val cardTemplateImageList: List<CardTemplateImageListItem?>? = null,
	@Expose
	@SerializedName("card")
	val card: Card? = null
)
