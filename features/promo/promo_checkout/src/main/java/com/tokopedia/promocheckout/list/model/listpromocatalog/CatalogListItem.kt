package com.tokopedia.promocheckout.list.model.listpromocatalog

import com.google.gson.annotations.SerializedName

data class CatalogListItem(

	@SerializedName("pointsStr")
	val pointsStr: String? = null,

	@SerializedName("baseCode")
	val baseCode: String? = null,

	@SerializedName("catalogType")
	val catalogType: Int? = null,

	@SerializedName("thumbnailURLMobile")
	val thumbnailURLMobile: String? = null,

	@SerializedName("expiredStr")
	val expiredStr: String? = null,

	@SerializedName("imageUrlMobile")
	val imageUrlMobile: String? = null,

	@SerializedName("title")
	val title: String? = null,

	@SerializedName("points")
	val points: Int? = null,

	@SerializedName("discountPercentage")
	val discountPercentage: Int? = null,

	@SerializedName("isDisabledButton")
	val isDisabledButton: Boolean? = null,

	@SerializedName("expired")
	val expired: String? = null,

	@SerializedName("expiredLabel")
	val expiredLabel: String? = null,

	@SerializedName("quota")
	val quota: Int? = null,

	@SerializedName("imageURL")
	val imageURL: String? = null,

	@SerializedName("pointsSlash")
	val pointsSlash: Int? = null,

	@SerializedName("isGift")
	val isGift: Int? = null,

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("imageV2URL")
	val imageV2URL: String? = null,

	@SerializedName("isDisabled")
	val isDisabled: Boolean? = null,

	@SerializedName("disableErrorMessage")
	val disableErrorMessage: String? = null,

	@SerializedName("pointsSlashStr")
	val pointsSlashStr: String? = null,

	@SerializedName("slug")
	val slug: String? = null,

	@SerializedName("thumbnailURL")
	val thumbnailURL: String? = null,

	@SerializedName("imageV2URLMobile")
	val imageV2URLMobile: String? = null,

	@SerializedName("discountPercentageStr")
	val discountPercentageStr: String? = null,

	@SerializedName("thumbnailV2URL")
	val thumbnailV2URL: String? = null,

	@SerializedName("isShowTukarButton")
	val isShowTukarButton: Boolean? = null,

	@SerializedName("subtitle")
	val subtitle: String? = null,

	@SerializedName("promoID")
	val promoID: Int? = null,

	@SerializedName("thumbnailV2URLMobile")
	val thumbnailV2URLMobile: String? = null,

	@SerializedName("upperTextDesc")
	val upperTextDesc: List<Any?>? = null,

	@SerializedName("quotaPercentage")
	val quotaPercentage: Int? = null
)