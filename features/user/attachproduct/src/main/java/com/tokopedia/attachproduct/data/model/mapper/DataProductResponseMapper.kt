package com.tokopedia.attachproduct.data.model.mapper

import com.tokopedia.attachproduct.data.model.DataProductResponse
import com.tokopedia.attachproduct.view.uimodel.NewAttachProductItemUiModel

fun DataProductResponse.mapToAttachUiModel(): NewAttachProductItemUiModel {
    return NewAttachProductItemUiModel(this.productUrl,
        this.productName, this.productId
        , this.productImageFull, this.productImage
        , this.productPrice, this.shop.shopName, this.originalPrice
        , this.discountPercentage, this.freeOngkir.isActive
        , this.freeOngkir.imageUrl, this.stock
    )
}
