package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.play.broadcaster.socket.PlaySocketEnum
import com.tokopedia.play.broadcaster.socket.PlaySocketType

/**
 * Created by jegul on 10/07/20
 */
data class ProductTagging(
        @SerializedName("products")
        val productList: List<Product>
) : PlaySocketType {

    override val type: PlaySocketEnum
        get() = PlaySocketEnum.ProductTag

    data class Product(
            @SerializedName("id")
            val id: Long,

            @SerializedName("image_url")
            val imageUrl: String,

            @SerializedName("is_available")
            val isAvailable: Boolean,

            @SerializedName("name")
            val name: String,

            @SerializedName("quantity")
            val quantity: Int
    )
}