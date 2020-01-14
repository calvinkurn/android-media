package com.tokopedia.shop.info.data.model

import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopRatingStats
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopSatisfaction

data class ShopStatisticsResp (
        val shopReputation: ShopBadge? = null,
        val shopPackSpeed: ProductShopPackSpeed? = null,
        val shopRatingStats: ShopRatingStats? = null,
        val shopSatisfaction: ShopSatisfaction? = null
)