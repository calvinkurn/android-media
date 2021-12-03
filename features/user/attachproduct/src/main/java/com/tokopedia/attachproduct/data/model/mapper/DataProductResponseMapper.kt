package com.tokopedia.attachproduct.data.model.mapper

import com.tokopedia.attachproduct.data.model.DataProductResponse
import com.tokopedia.attachproduct.view.uimodel.AttachProductItemUiModel

fun DataProductResponse.mapToAttachUiModel(): AttachProductItemUiModel {
    return AttachProductItemUiModel(this.productUrl,
        this.productName, this.productId
        , this.productImageFull, this.productImage
        , this.productPrice, this.shop.shopName, this.originalPrice
        , this.discountPercentage, this.freeOngkir.isActive
        , this.freeOngkir.imageUrl, this.stock, this.childs.isNotEmpty()
        , this.isPreorder, this.priceInt.toLong(), this.categoryId
    )
}
