package com.tokopedia.vouchercreation.shop.voucherlist.model.remote

import com.google.gson.annotations.SerializedName

data class GetBroadCastMetaDataResponse(
    @SerializedName("chatBlastSellerMetadata")
    val chatBlastSellerMetadata: ChatBlastSellerMetadata = ChatBlastSellerMetadata()
)

data class ChatBlastSellerMetadata(
    @SerializedName("promo")
    val promo: Int = 0,
    @SerializedName("status")
    val status: Int = 0
)