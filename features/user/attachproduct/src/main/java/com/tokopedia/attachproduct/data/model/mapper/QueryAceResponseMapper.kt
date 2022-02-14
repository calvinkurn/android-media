package com.tokopedia.attachproduct.data.model.mapper

import com.tokopedia.attachproduct.data.model.AceSearchProductResponse
import com.tokopedia.attachproduct.domain.model.AttachProductDomainModel

fun AceSearchProductResponse.mapToListProduct(): AttachProductDomainModel {
    return AttachProductDomainModel(
        this.aceSearchProductResponse.data.products.toList())
}


