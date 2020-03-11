package com.tokopedia.product.manage.feature.multiedit.data.param

import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

data class ProductParam(
    val productId: String,
    val shop: ShopParam,
    val stock: Int? = null,
    val price: Int? = null,
    val menu: MenuParam? = null,
    val status: ProductStatus? = null
)