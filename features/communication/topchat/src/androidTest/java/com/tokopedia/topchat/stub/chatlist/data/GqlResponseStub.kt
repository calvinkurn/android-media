package com.tokopedia.topchat.stub.chatlist.data

import com.tokopedia.topchat.chatlist.domain.pojo.chatblastseller.BlastSellerMetaDataResponse

object GqlResponseStub {

    lateinit var blastSellerMetaDataResponse: ResponseStub<BlastSellerMetaDataResponse>

    init {
        reset()
    }

    fun reset() {
        blastSellerMetaDataResponse = ResponseStub(
            filePath = "seller/success_get_chat_blast_seller_meta_data.json",
            type = BlastSellerMetaDataResponse::class.java,
            query = "chatBlastSellerMetadata",
            isError = false
        )
    }
}
