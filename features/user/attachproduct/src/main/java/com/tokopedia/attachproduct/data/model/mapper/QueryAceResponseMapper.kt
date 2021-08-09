package com.tokopedia.attachproduct.data.model.mapper

import com.tokopedia.attachproduct.data.model.AceSearchProductResponse
import com.tokopedia.attachproduct.domain.model.NewAttachProductDomainModel

fun AceSearchProductResponse.mapToListProduct(): NewAttachProductDomainModel {
    return NewAttachProductDomainModel(
        this.aceSearchProductResponse.data?.products?.toList()
                ?: emptyList())
}


