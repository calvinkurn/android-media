package com.tokopedia.attachproduct.domain.model

import com.tokopedia.attachproduct.data.model.DataProductResponse

data class NewAttachProductDomainModel(
    var products: List<DataProductResponse> = emptyList()
)
