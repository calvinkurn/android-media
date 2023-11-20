package com.tokopedia.universal_sharing.stub.data.response

import com.tokopedia.universal_sharing.data.model.UniversalSharingPostPurchaseProductWrapperResponse

object GqlResponseStub {

    lateinit var productV3Response: ResponseStub<UniversalSharingPostPurchaseProductWrapperResponse>

    init {
        reset()
    }

    fun reset() {
        productV3Response = ResponseStub(
            filePath = "product/success_get_productv3.json",
            type = UniversalSharingPostPurchaseProductWrapperResponse::class.java,
            query = "getProductV3",
            error = null
        )
    }
}
