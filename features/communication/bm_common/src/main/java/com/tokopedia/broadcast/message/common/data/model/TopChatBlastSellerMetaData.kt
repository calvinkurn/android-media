package com.tokopedia.broadcast.message.common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TopChatBlastSellerMetaData(
        @SerializedName("shopId")
        @Expose
        val shopId: Long = 0L,

        @SerializedName("quota")
        @Expose
        val quota: Int = 0,

        @SerializedName("expireAt")
        @Expose
        val expireAt: String = "",

        @SerializedName("status")
        @Expose
        val status: Int = 0
) {
    val hasActiveQuota: Boolean
        get() = status == 1 && quota > 0

    data class Response(
            @SerializedName("chatBlastSellerMetadata")
            @Expose
            val result: TopChatBlastSellerMetaData? = null
    )
}