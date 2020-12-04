package com.tokopedia.product.manage.common.feature.broadcastchat.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChatBlastSellerMetaDataResponse(
        @SerializedName("chatBlastSellerMetadata")
        @Expose
        val chatBlastSellerMetadata: ChatBlastSellerMetadata? = ChatBlastSellerMetadata()
) {
    data class ChatBlastSellerMetadata(
            @SerializedName("url")
            @Expose
            val url: String? = ""
    )
}