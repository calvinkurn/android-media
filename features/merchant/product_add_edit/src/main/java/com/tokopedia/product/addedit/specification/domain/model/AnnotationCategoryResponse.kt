package com.tokopedia.product.addedit.specification.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AnnotationCategoryResponse (
		@SerializedName("drogonAnnotationCategoryV2")
		@Expose
		val drogonAnnotationCategoryV2 : DrogonAnnotationCategoryV2
)

data class DrogonAnnotationCategoryV2 (
		@SerializedName("data")
		@Expose
		val data : List<AnnotationCategoryData>
)

data class AnnotationCategoryData (
		@SerializedName("sortOrder")
		@Expose
		val sortOrder : Int = 0,

		@SerializedName("variant")
		@Expose
		val variant : String = "",

		@SerializedName("values")
		@Expose
		val data : List<Values> = emptyList()
)

data class Values (
		@SerializedName("id")
		@Expose
		val id : Int,

		@SerializedName("name")
		@Expose
		val name : String,

		@SerializedName("selected")
		@Expose
		val selected : Boolean,

		@SerializedName("data")
		@Expose
		val data : String
)