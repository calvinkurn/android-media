package com.tokopedia.product.detail.data.model

import com.tokopedia.product.detail.common.data.model.carttype.CartRedirectionResponse
import com.tokopedia.product.detail.common.data.model.warehouse.MultiOriginWarehouse
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.variant_common.model.VariantMultiOriginWarehouse

/* Request P2 relates to Shop that will be used as Parameter of P3 Request */

data class ProductInfoP2ShopData(
        var shopInfo: ShopInfo? = null,
        var shopCod: Boolean = false,
        var nearestWarehouse: MultiOriginWarehouse = MultiOriginWarehouse(),
        var tradeinResponse: TradeinResponse? = null,
        var cartRedirectionResponse: CartRedirectionResponse = CartRedirectionResponse(),
        var variantMultiOrigin: VariantMultiOriginWarehouse = VariantMultiOriginWarehouse(),
        var tickerInfo: List<StickyLoginTickerPojo.TickerDetail> = ArrayList(),
        var shopSpeed: Int = 0,
        var shopChatSpeed: Int = 0,
        var shopRating: Float = 0F
)