package com.tokopedia.buyerorder.detail.data.recommendationPojo

import com.google.gson.annotations.SerializedName

data class WidgetGridItem(

	@SerializedName("original_price")
	val originalPrice: String?,

	@SerializedName("applink")
	val applink: String?,

	@SerializedName("image_url")
	val imageUrl: String?,

	@SerializedName("tag_name")
	val tagName: String?,

	@SerializedName("tag_type")
	val tagType: Int?,

	@SerializedName("price_prefix")
	val pricePrefix: String?,

	@SerializedName("desc_1")
	val descFirst: String?,

	@SerializedName("desc_2")
	val descSecond: String?,

	@SerializedName("url")
	val url: String?,

	@SerializedName("price")
	val price: String?,

	@SerializedName("name")
	val name: String?,

	@SerializedName("template_id")
	val templateId: Int=0,

	@SerializedName("id")
	val id: Int=0,

	@SerializedName("title_1")
	val titleFirst: String?,

	@SerializedName("title_2")
	val titleSecond: String?
)
