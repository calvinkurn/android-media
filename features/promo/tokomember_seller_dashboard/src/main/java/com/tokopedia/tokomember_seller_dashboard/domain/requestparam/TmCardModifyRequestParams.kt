package com.tokopedia.tokomember_seller_dashboard.domain.requestparam

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmCardModifyRequestParams(
	@Expose
	@SerializedName("input")
	val input: TmCardModifyInput? = null
)

data class Card(
	@Expose
	@SerializedName("tierGroupID")
	val tierGroupID: Int? = 0,
	@Expose
	@SerializedName("name")
	val name: String? = "",
	@Expose
	@SerializedName("id")
	val id: Int? = 0,
	@Expose
	@SerializedName("shopID")
	val shopID: Int? = 0,
	@Expose
	@SerializedName("numberOfLevel")
	val numberOfLevel: Int? = 0
)

data class TmCardModifyInput(
	@Expose
	@SerializedName("apiVersion")
	val apiVersion: String? = "",
	@Expose
	@SerializedName("isMerchantCard")
	val isMerchantCard: Boolean? = false,
	@Expose
	@SerializedName("cardTemplate")
	val cardTemplate: CardTemplate? = null,
	@Expose
	@SerializedName("intoolsShop")
	val intoolsShop: IntoolsShop? = null,
	@Expose
	@SerializedName("card")
	val card: Card? = null
)

data class IntoolsShop(
	@Expose
	@SerializedName("id")
	val id: Int? = 0
)

data class CardTemplate(
	@Expose
	@SerializedName("backgroundColor")
	val backgroundColor: String? = "",
	@Expose
	@SerializedName("cardID")
	val cardID: Int? = 0,
	@Expose
	@SerializedName("id")
	val id: Int? = 0,
	@Expose
	@SerializedName("fontColor")
	val fontColor: String? = "",
	@Expose
	@SerializedName("backgroundImgUrl")
	val backgroundImgUrl: String? = ""
)
