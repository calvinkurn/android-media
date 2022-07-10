package com.tokopedia.tokopedianow.recipebookmark.domain.model

import com.google.gson.annotations.SerializedName

data class GetRecipeBookmarksResponse(
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("TokonowGetRecipeBookmarks")
        val tokonowGetRecipeBookmarks: TokonowGetRecipeBookmarks
    ) {
        data class TokonowGetRecipeBookmarks(
            @SerializedName("data")
            val data: DataX,
            @SerializedName("header")
            val header: Header,
            @SerializedName("metadata")
            val metadata: Metadata
        ) {
            data class DataX(
                @SerializedName("recipes")
                val recipes: List<Recipe>,
                @SerializedName("user_id")
                val userId: String
            ) {
                data class Recipe(
                    @SerializedName("category")
                    val category: Category,
                    @SerializedName("created_time")
                    val createdTime: String,
                    @SerializedName("duration")
                    val duration: Int,
                    @SerializedName("id")
                    val id: String,
                    @SerializedName("images")
                    val images: List<Image>,
                    @SerializedName("ingredients")
                    val ingredients: List<Ingredient>,
                    @SerializedName("instruction")
                    val instruction: String,
                    @SerializedName("portion")
                    val portion: Int,
                    @SerializedName("products")
                    val products: List<Product>,
                    @SerializedName("published_time")
                    val publishedTime: String,
                    @SerializedName("status")
                    val status: String,
                    @SerializedName("tags")
                    val tags: List<Tag>,
                    @SerializedName("title")
                    val title: String,
                    @SerializedName("updated_time")
                    val updatedTime: String,
                    @SerializedName("videos")
                    val videos: List<Video>
                ) {
                    data class Image(
                        @SerializedName("file_name")
                        val fileName: String,
                        @SerializedName("file_path")
                        val filePath: String,
                        @SerializedName("h")
                        val h: Int,
                        @SerializedName("url_original")
                        val urlOriginal: String,
                        @SerializedName("url_thumbnail")
                        val urlThumbnail: String,
                        @SerializedName("w")
                        val w: Int
                    )

                    data class Product(
                        @SerializedName("app_url")
                        val appUrl: String,
                        @SerializedName("discount_percentage")
                        val discountPercentage: Long,
                        @SerializedName("id")
                        val id: String,
                        @SerializedName("image_url")
                        val imageUrl: String,
                        @SerializedName("max_order")
                        val maxOrder: Int,
                        @SerializedName("min_order")
                        val minOrder: Int,
                        @SerializedName("name")
                        val name: String,
                        @SerializedName("price")
                        val price: Double,
                        @SerializedName("quantity")
                        val quantity: Int,
                        @SerializedName("slashed_price")
                        val slashedPrice: Double,
                        @SerializedName("stock")
                        val stock: Int,
                        @SerializedName("url")
                        val url: String
                    )

                    data class Video(
                        @SerializedName("type")
                        val type: String,
                        @SerializedName("url")
                        val url: String
                    )

                    data class Ingredient(
                        @SerializedName("image")
                        val image: Image,
                        @SerializedName("name")
                        val name: String,
                        @SerializedName("quantity")
                        val quantity: Double,
                        @SerializedName("unit")
                        val unit: String
                    )

                    data class Category(
                        @SerializedName("id")
                        val id: String,
                        @SerializedName("name")
                        val name: String
                    )

                    data class Tag(
                        @SerializedName("id")
                        val id: String,
                        @SerializedName("name")
                        val name: String
                    )
                }
            }
            data class Header(
                @SerializedName("error_code")
                val errorCode: String,
                @SerializedName("message")
                val message: String,
                @SerializedName("process_time")
                val processTime: Double,
                @SerializedName("reason")
                val reason: String
            )

            data class Metadata(
                @SerializedName("hasNext")
                val hasNext: Boolean,
                @SerializedName("total")
                val total: Int
            )
        }
    }

}



