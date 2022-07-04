package com.tokopedia.tokopedianow.recipebookmark.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetRecipeBookmarksResponse(
    @Expose
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @Expose
        @SerializedName("TokonowGetRecipeBookmarks")
        val tokonowGetRecipeBookmarks: TokonowGetRecipeBookmarks
    ) {
        data class TokonowGetRecipeBookmarks(
            @Expose
            @SerializedName("data")
            val data: DataX,
            @Expose
            @SerializedName("header")
            val header: Header,
            @Expose
            @SerializedName("metadata")
            val metadata: Metadata
        ) {
            data class DataX(
                @Expose
                @SerializedName("recipes")
                val recipes: List<Recipe>,
                @Expose
                @SerializedName("user_id")
                val userId: String
            ) {
                data class Recipe(
                    @Expose
                    @SerializedName("category")
                    val category: Category,
                    @Expose
                    @SerializedName("created_time")
                    val createdTime: String,
                    @Expose
                    @SerializedName("duration")
                    val duration: Int,
                    @Expose
                    @SerializedName("id")
                    val id: String,
                    @Expose
                    @SerializedName("images")
                    val images: List<Image>,
                    @Expose
                    @SerializedName("ingredients")
                    val ingredients: List<Ingredient>,
                    @Expose
                    @SerializedName("instruction")
                    val instruction: String,
                    @Expose
                    @SerializedName("portion")
                    val portion: Int,
                    @Expose
                    @SerializedName("products")
                    val products: List<Product>,
                    @Expose
                    @SerializedName("published_time")
                    val publishedTime: String,
                    @Expose
                    @SerializedName("status")
                    val status: String,
                    @Expose
                    @SerializedName("tags")
                    val tags: List<Tag>,
                    @Expose
                    @SerializedName("title")
                    val title: String,
                    @Expose
                    @SerializedName("updated_time")
                    val updatedTime: String,
                    @Expose
                    @SerializedName("videos")
                    val videos: List<Video>
                ) {
                    data class Image(
                        @Expose
                        @SerializedName("file_name")
                        val fileName: String,
                        @Expose
                        @SerializedName("file_path")
                        val filePath: String,
                        @Expose
                        @SerializedName("h")
                        val h: Int,
                        @Expose
                        @SerializedName("url_original")
                        val urlOriginal: String,
                        @Expose
                        @SerializedName("url_thumbnail")
                        val urlThumbnail: String,
                        @Expose
                        @SerializedName("w")
                        val w: Int
                    )

                    data class Product(
                        @Expose
                        @SerializedName("app_url")
                        val appUrl: String,
                        @Expose
                        @SerializedName("discount_percentage")
                        val discountPercentage: Long,
                        @Expose
                        @SerializedName("id")
                        val id: String,
                        @Expose
                        @SerializedName("image_url")
                        val imageUrl: String,
                        @Expose
                        @SerializedName("max_order")
                        val maxOrder: Int,
                        @Expose
                        @SerializedName("min_order")
                        val minOrder: Int,
                        @Expose
                        @SerializedName("name")
                        val name: String,
                        @Expose
                        @SerializedName("price")
                        val price: Double,
                        @Expose
                        @SerializedName("quantity")
                        val quantity: Int,
                        @Expose
                        @SerializedName("slashed_price")
                        val slashedPrice: Double,
                        @Expose
                        @SerializedName("stock")
                        val stock: Int,
                        @Expose
                        @SerializedName("url")
                        val url: String
                    )

                    data class Video(
                        @Expose
                        @SerializedName("type")
                        val type: String,
                        @Expose
                        @SerializedName("url")
                        val url: String
                    )

                    data class Ingredient(
                        @Expose
                        @SerializedName("image")
                        val image: Image,
                        @Expose
                        @SerializedName("name")
                        val name: String,
                        @Expose
                        @SerializedName("quantity")
                        val quantity: Double,
                        @Expose
                        @SerializedName("unit")
                        val unit: String
                    )

                    data class Category(
                        @Expose
                        @SerializedName("id")
                        val id: String,
                        @Expose
                        @SerializedName("name")
                        val name: String
                    )

                    data class Tag(
                        @Expose
                        @SerializedName("id")
                        val id: String,
                        @Expose
                        @SerializedName("name")
                        val name: String
                    )
                }
            }
            data class Header(
                @Expose
                @SerializedName("error_code")
                val errorCode: String,
                @Expose
                @SerializedName("message")
                val message: String,
                @Expose
                @SerializedName("process_time")
                val processTime: Double,
                @Expose
                @SerializedName("reason")
                val reason: String
            )

            data class Metadata(
                @Expose
                @SerializedName("hasNext")
                val hasNext: Boolean,
                @Expose
                @SerializedName("total")
                val total: Int
            )
        }
    }

}



