package com.tokopedia.promocheckout.detail.model.detailmodel

import com.google.gson.annotations.SerializedName


data class HachikoCatalogDetail(

	@SerializedName("button_str")
	val buttonStr: String? = null,

	@SerializedName("points_slash_str")
	val pointsSlashStr: String? = null,

	@SerializedName("expired_label")
	val expiredLabel: String? = null,

	@SerializedName("icon")
	val icon: String? = null,

	@SerializedName("description")
	val description: String? = null,

	@SerializedName("discount_percentage_str")
	val discountPercentageStr: String? = null,

	@SerializedName("minimumUsageLabel")
	val minimumUsageLabel: String? = null,

	@SerializedName("how_to_use")
	val howToUse: String? = null,

	@SerializedName("discount_percentage")
	val discountPercentage: Int? = null,

	@SerializedName("title")
	val title: String? = null,

	@SerializedName("thumbnail_url")
	val thumbnailUrl: String? = null,

	@SerializedName("thumbnail_v2_url_mobile")
	val thumbnailV2UrlMobile: String? = null,

	@SerializedName("points")
	val points: Int? = null,

	@SerializedName("cta")
	val cta: String? = null,

	@SerializedName("expired")
	val expired: String? = null,

	@SerializedName("image_v2_url_mobile")
	val imageV2UrlMobile: String? = null,

	@SerializedName("expired_str")
	val expiredStr: String? = null,

	@SerializedName("quota")
	val quota: Int? = null,

	@SerializedName("thumbnail_v2_url")
	val thumbnailV2Url: String? = null,

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("points_str")
	val pointsStr: String? = null,

	@SerializedName("points_slash")
	val pointsSlash: Int? = null,

	@SerializedName("overview")
	val overview: String? = null,

	@SerializedName("sub_title")
	val subTitle: String? = null,

	@SerializedName("thumbnail_url_mobile")
	val thumbnailUrlMobile: String? = null,

	@SerializedName("image_url")
	val imageUrl: String? = null,

	@SerializedName("is_disabled")
	val isDisabled: Boolean? = null,

	@SerializedName("disable_error_message")
	val disableErrorMessage: String? = null,

	@SerializedName("tnc")
	val tnc: String? = null,

	@SerializedName("is_disabled_button")
	val isDisabledButton: Boolean? = null,

	@SerializedName("image_url_mobile")
	val imageUrlMobile: String? = null,

	@SerializedName("is_gift")
	val isGift: Int? = null,

	@SerializedName("upper_text_desc")
	val upperTextDesc: List<String?>? = null,

	@SerializedName("catalog_type")
	val catalogType: Int? = null,

	@SerializedName("image_v2_url")
	val imageV2Url: String? = null,

	@SerializedName("minimumUsage")
	val minimumUsage: String? = null,

	@SerializedName("quotaPercentage")
	val quotaPercentage: Int? = null
)