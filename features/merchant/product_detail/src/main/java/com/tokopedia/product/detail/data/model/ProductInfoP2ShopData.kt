package com.tokopedia.product.detail.data.model

import com.tokopedia.product.detail.common.data.model.warehouse.MultiOriginWarehouse
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo

/* Request P2 relates to Shop that will be used as Parameter of P3 Request */

data class ProductInfoP2ShopData (
        var shopInfo: ShopInfo? = null,
        var shopCod: Boolean = false,
        var nearestWarehouse: MultiOriginWarehouse = MultiOriginWarehouse()
        )