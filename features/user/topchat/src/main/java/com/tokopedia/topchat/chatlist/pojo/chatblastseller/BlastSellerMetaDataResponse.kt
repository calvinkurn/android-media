package com.tokopedia.topchat.chatlist.pojo.chatblastseller


import com.google.gson.annotations.SerializedName

data class BlastSellerMetaDataResponse(
    @SerializedName("chatBlastSellerMetadata")
    val chatBlastSellerMetadata: ChatBlastSellerMetadata = ChatBlastSellerMetadata()
)