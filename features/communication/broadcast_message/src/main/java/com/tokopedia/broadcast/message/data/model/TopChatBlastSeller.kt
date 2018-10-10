package com.tokopedia.broadcast.message.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.broadcast.message.view.adapter.BroadcastMessageTypeFactory

data class TopChatBlastSeller(
        @SerializedName("blastId")
        @Expose
        val blastId: Int = -1,

        @SerializedName("executionTime")
        @Expose
        val executionTime: String = "",

        @SerializedName("message")
        @Expose
        val message: String = "",

        @SerializedName("marketingThumbnail")
        @Expose
        val marketingThumbnail: AttachmentMarketingThumbnail? = null,

        @SerializedName("products")
        @Expose
        val products: List<AttachmentProduct>? = null,

        @SerializedName("state")
        @Expose
        var state: State? = null
): Visitable<BroadcastMessageTypeFactory> {
    override fun type(typeFactory: BroadcastMessageTypeFactory) = typeFactory.type(this)

    data class AttachmentMarketingThumbnail(
            @SerializedName("attributes")
            @Expose
            val attributes: AttachmentAttributesMarketingThumbnail? = null
    )

    data class AttachmentProduct(
            @SerializedName("attributes")
            @Expose
            val attributes: AttachmentAttributesProduct? = null
    )

    data class AttachmentAttributesMarketingThumbnail(
            @SerializedName("url")
            @Expose
            val url: String? = null,

            @SerializedName("imageUrl")
            @Expose
            val imageUrl: String? = null
    )

    data class AttachmentAttributesProduct(
            @SerializedName("productId")
            @Expose
            val productId: Int = 0,

            @SerializedName("productProfile")
            @Expose
            val productProfile: ProductProfile? = null
    )

    data class ProductProfile(
            @SerializedName("name")
            @Expose
            val name: String? = null,

            @SerializedName("price")
            @Expose
            val price: String? = null,

            @SerializedName("url")
            @Expose
            val url: String? = null,

            @SerializedName("imageUrl")
            @Expose
            val imageUrl: String? = null
    )

    data class State(
            @SerializedName("status")
            @Expose
            val status: Int = 0,

            @SerializedName("totalSent")
            @Expose
            val totalSent: Int = 0,

            @SerializedName("totalRead")
            @Expose
            val totalRead: Int = 0,

            @SerializedName("totalTarget")
            @Expose
            val totalTarget: Int = 0
    )

    data class BlastSellerList (
            @SerializedName("list")
            @Expose
            val list: List<TopChatBlastSeller>? = null,

            @SerializedName("hasNext")
            @Expose
            val hasNext: Boolean = false,

            @SerializedName("currentPage")
            @Expose
            val currentPage: Int = 0
    )

    data class Response (@SerializedName("chatBlastSellerList")
                         @Expose val result: BlastSellerList? = null)
}