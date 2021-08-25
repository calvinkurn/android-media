package com.tokopedia.attachproduct.data.model.mapper

import com.tokopedia.attachproduct.data.model.NewDataProductResponse
import com.tokopedia.attachproduct.view.uimodel.NewAttachProductItemUiModel

fun NewDataProductResponse.mapToAttachUiModel(): NewAttachProductItemUiModel {
    return NewAttachProductItemUiModel(this.productUrl,
        this.productName, this.productId
        , this.productImageFull, this.productImage
        , this.productPrice, this.shop.shopName, this.originalPrice, this.discountPercentage
    )
}
