package com.tokopedia.attachproduct.domain.model

import com.tokopedia.attachproduct.data.model.NewDataProductResponse

data class NewAttachProductDomainModel(
    var productNews: List<NewDataProductResponse> = emptyList()
)