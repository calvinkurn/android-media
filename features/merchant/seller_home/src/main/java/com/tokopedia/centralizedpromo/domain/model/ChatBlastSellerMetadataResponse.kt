package com.tokopedia.centralizedpromo.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChatBlastSellerMetadataResponse(
        @SerializedName("chatBlastSellerMetadata")
        @Expose
        val chatBlastSellerMetadata: ChatBlastSellerMetadata?
)

data class ChatBlastSellerMetadata(
        @SerializedName("promo")
        @Expose
        val promo: Int?,
        @SerializedName("promoType")
        @Expose
        val promoType: Int?,
        @SerializedName("url")
        @Expose
        val url: String?
)