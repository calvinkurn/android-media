package com.tokopedia.product.addedit.common.domain.model.responses

data class ProductAddEditV3SuccessFailedResponse(
        var failedResponse: MutableList<ProductAddEditV3Response> = mutableListOf(),
        var successResponse: MutableList<ProductAddEditV3Response> = mutableListOf()
)