package com.tokopedia.sellerapp.dashboard.model;

import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfoTxStats;
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge;
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo;
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopRatingStats;
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopSatisfaction;

public class ShopInfoDashboardModel {
    private ShopInfo shopInfo;
    private ShopInfoTxStats shopInfoTxStats;
    private ShopBadge shopReputation;
    private ShopRatingStats shopRatingStats;
    private ShopSatisfaction shopSatisfaction;

    public ShopInfoDashboardModel(
            ShopInfo shopInfo,
            ShopInfoTxStats shopInfoTxStats,
            ShopBadge shopReputation,
            ShopRatingStats shopRatingStats,
            ShopSatisfaction shopSatisfaction
    ) {
        this.shopInfo = shopInfo;
        this.shopInfoTxStats = shopInfoTxStats;
        this.shopReputation = shopReputation;
        this.shopRatingStats = shopRatingStats;
        this.shopSatisfaction = shopSatisfaction;
    }

    public ShopInfo getShopInfo() {
        return shopInfo;
    }

    public ShopInfoTxStats getShopInfoTxStats() {
        return shopInfoTxStats;
    }

    public ShopBadge getShopReputation() {
        return shopReputation;
    }

    public ShopRatingStats getShopRatingStats() {
        return shopRatingStats;
    }

    public ShopSatisfaction getShopSatisfaction() {
        return shopSatisfaction;
    }
}

