package com.tokopedia.product.addedit.specification.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AnnotationCategoryResponse (
		@SerializedName("drogonAnnotationCategoryV2")
		@Expose
		val drogonAnnotationCategoryV2 : DrogonAnnotationCategoryV2
)

data class DrogonAnnotationCategoryV2 (
		@SerializedName("header")
		@Expose
		val header : Header,

		@SerializedName("categoryID")
		@Expose
		val categoryID : Int,

		@SerializedName("productID")
		@Expose
		val productID : Int,

		@SerializedName("vendorName")
		@Expose
		val vendorName : String,

		@SerializedName("data")
		@Expose
		val data : List<AnnotationCategoryData>
)

data class Header (
		@SerializedName("processTime")
		@Expose
		val processTime : String,

		@SerializedName("messages")
		@Expose
		val messages : List<String>,

		@SerializedName("reason")
		@Expose
		val reason : String,

		@SerializedName("errorCode")
		@Expose
		val errorCode : String
)

data class AnnotationCategoryData (
		@SerializedName("sortOrder")
		@Expose
		val sortOrder : Int,

		@SerializedName("variant")
		@Expose
		val variant : String,

		@SerializedName("values")
		@Expose
		val data : List<Values>
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
		val data : String,

		@SerializedName("isAgg")
		@Expose
		val isAgg : Boolean
)