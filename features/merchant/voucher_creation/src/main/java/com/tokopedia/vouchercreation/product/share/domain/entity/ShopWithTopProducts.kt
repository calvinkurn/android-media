package com.tokopedia.vouchercreation.product.share.domain.entity

import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult

data class ShopWithTopProducts(val topProductsImageUrl : List<String>, val shop : ShopBasicDataResult)
