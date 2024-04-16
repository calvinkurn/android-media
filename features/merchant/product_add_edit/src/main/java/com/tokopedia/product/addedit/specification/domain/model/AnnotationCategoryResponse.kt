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
        @SerializedName("variantId")
        @Expose
        val variantId : Long = 0,

		@SerializedName("sortOrder")
		@Expose
		val sortOrder : Int = 0,

		@SerializedName("variant")
		@Expose
		val variant : String = "",

		@SerializedName("values")
		@Expose
		val data : List<Values> = emptyList(),

        @SerializedName("isMandatory")
        @Expose
        val isMandatory : Boolean = false,

        @SerializedName("isCustomAnnotType")
        @Expose
        val isCustomAnnotType : Boolean = false
)

data class Values (
		@SerializedName("id")
		@Expose
		val id : String,

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
