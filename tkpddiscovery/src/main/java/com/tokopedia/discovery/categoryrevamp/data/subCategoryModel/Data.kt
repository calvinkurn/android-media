package com.tokopedia.discovery.categoryrevamp.data.subCategoryModel

import com.google.gson.annotations.SerializedName

data class Data(

	@field:SerializedName("widgetVideo")
	val widgetVideo: Boolean? = null,

	@field:SerializedName("parent")
	val parent: Int? = null,

	@field:SerializedName("staticHeaderImageHexColor")
	val staticHeaderImageHexColor: String? = null,

	@field:SerializedName("rootId")
	val rootId: Int? = null,

	@field:SerializedName("widgetBannerPromo")
	val widgetBannerPromo: Boolean? = null,

	@field:SerializedName("tree")
	val tree: Int? = null,

	@field:SerializedName("widgetSubCategory")
	val widgetSubCategory: Boolean? = null,

	@field:SerializedName("widgetHotlist")
	val widgetHotlist: Boolean? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("titleTag")
	val titleTag: String? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("widgetOfficialStore")
	val widgetOfficialStore: Boolean? = null,

	@field:SerializedName("applinks")
	val applinks: String? = null,

	@field:SerializedName("brandId")
	val brandId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("curatedProductDepId")
	val curatedProductDepId: Int? = null,

	@field:SerializedName("widgetOtherSubCategory")
	val widgetOtherSubCategory: Boolean? = null,

	@field:SerializedName("isAdult")
	val isAdult: Int? = null,

	@field:SerializedName("curatedProductImageUrl")
	val curatedProductImageUrl: String? = null,

	@field:SerializedName("status")
	val status: Any? = null,

	@field:SerializedName("staticHeaderImage")
	val staticHeaderImage: String? = null,

	@field:SerializedName("child")
	val child: List<SubCategoryItem?>? = null
)