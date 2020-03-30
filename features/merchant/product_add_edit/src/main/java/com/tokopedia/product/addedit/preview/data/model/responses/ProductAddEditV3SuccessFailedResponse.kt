package com.tokopedia.product.addedit.preview.data.model.responses

data class ProductAddEditV3SuccessFailedResponse(
        var failedResponse: MutableList<ProductAddEditV3Response> = mutableListOf(),
        var successResponse: MutableList<ProductAddEditV3Response> = mutableListOf()
)