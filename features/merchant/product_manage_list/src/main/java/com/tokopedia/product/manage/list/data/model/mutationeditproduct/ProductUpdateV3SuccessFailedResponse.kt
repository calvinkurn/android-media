package com.tokopedia.product.manage.list.data.model.mutationeditproduct

data class ProductUpdateV3SuccessFailedResponse(
        var failedResponse: MutableList<ProductUpdateV3Response> = mutableListOf(),
        var successResponse: MutableList<ProductUpdateV3Response> = mutableListOf()
)