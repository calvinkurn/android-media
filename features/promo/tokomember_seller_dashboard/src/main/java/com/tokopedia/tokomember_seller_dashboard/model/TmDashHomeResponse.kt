package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.SerializedName

data class TmDashHomeResponse(

	@SerializedName("membershipGetSellerAnalyticsTopSection")
	val membershipGetSellerAnalyticsTopSection: MembershipGetSellerAnalyticsTopSection? = null
)

data class HomeCardTemplate(

	@SerializedName("backgroundColor")
	val backgroundColor: String? = null,

	@SerializedName("cardID")
	val cardID: Int? = null,

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("fontColor")
	val fontColor: String? = null,

	@SerializedName("backgroundImgUrl")
	val backgroundImgUrl: String? = null
)

data class HomeCard(

	@SerializedName("tierGroupID")
	val tierGroupID: Int? = null,

	@SerializedName("intro")
	val intro: String? = null,

	@SerializedName("name")
	val name: String? = null,

	@SerializedName("limit")
	val limit: Int? = null,

	@SerializedName("parentIDs")
	val parentIDs: List<Any?>? = null,

	@SerializedName("index")
	val index: Int? = null,

	@SerializedName("numberOfLevelStr")
	val numberOfLevelStr: String? = null,

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("shopID")
	val shopID: Int? = null,

	@SerializedName("numberOfLevel")
	val numberOfLevel: Int? = null,

	@SerializedName("status")
	val status: Int? = null
)

data class HomeShop(

	@SerializedName("appLink")
	val appLink: String? = null,

	@SerializedName("shopStatusIconUrl")
	val shopStatusIconUrl: String? = null,

	@SerializedName("name")
	val name: String? = null,

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("avatar")
	val avatar: String? = null,

	@SerializedName("type")
	val type: Int? = null,

	@SerializedName("url")
	val url: String? = null
)

data class MembershipGetSellerAnalyticsTopSection(

	@SerializedName("resultStatus")
	val resultStatus: ResultStatus? = null,

	@SerializedName("ticker")
	val ticker: List<TickerItem?>? = null,

	@SerializedName("shopProfile")
	val shopProfile: ShopProfile? = null
)

data class ShopProfile(

	@SerializedName("shop")
	val shop: HomeShop? = null,

	@SerializedName("cardTemplate")
	val homeCardTemplate: HomeCardTemplate? = null,

	@SerializedName("card")
	val homeCard: HomeCard? = null
)

data class TickerItem(

	@SerializedName("cta")
	val cta: Cta? = null,

	@SerializedName("iconImageUrl")
	val iconImageUrl: String? = null,

	@SerializedName("description")
	val description: String? = null,

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("title")
	val title: String? = null,

	@SerializedName("isDismissable")
	val isDismissable: Boolean? = null
)

data class Cta(

	@SerializedName("appLink")
	val appLink: String? = null,

	@SerializedName("icon")
	val icon: String? = null,

	@SerializedName("isShown")
	val isShown: Boolean? = null,

	@SerializedName("text")
	val text: String? = null,

	@SerializedName("isDisabled")
	val isDisabled: Boolean? = null,

	@SerializedName("position")
	val position: String? = null,

	@SerializedName("urlMobile")
	val urlMobile: String? = null,

	@SerializedName("type")
	val type: String? = null,

	@SerializedName("url")
	val url: String? = null
)
