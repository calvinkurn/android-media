package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail;

/**
 * @author by nisie on 8/23/17.
 */

public class ShopDataDomain {

    private long shopId;
    private long shopUserId;
    private String domain;
    private String shopName;
    private String shopUrl;
    private String logo;
    private ShopReputationDomain shopReputation;

    public ShopDataDomain(long shopId, long shopUserId, String domain, String shopName,
                          String shopUrl, String logo, ShopReputationDomain shopReputation) {
        this.shopId = shopId;
        this.shopUserId = shopUserId;
        this.domain = domain;
        this.shopName = shopName;
        this.shopUrl = shopUrl;
        this.logo = logo;
        this.shopReputation = shopReputation;
    }

    public long getShopId() {
        return shopId;
    }

    public long getShopUserId() {
        return shopUserId;
    }

    public String getDomain() {
        return domain;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public String getLogo() {
        return logo;
    }

    public ShopReputationDomain getShopReputation() {
        return shopReputation;
    }
}
