package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail;

/**
 * @author by nisie on 8/23/17.
 */

public class ShopDataDomain {

    private int shopId;
    private int shopUserId;
    private String domain;
    private String shopName;
    private String shopUrl;
    private String logo;
    private ShopReputationDomain shopReputation;

    public ShopDataDomain(int shopId, int shopUserId, String domain, String shopName,
                          String shopUrl, String logo, ShopReputationDomain shopReputation) {
        this.shopId = shopId;
        this.shopUserId = shopUserId;
        this.domain = domain;
        this.shopName = shopName;
        this.shopUrl = shopUrl;
        this.logo = logo;
        this.shopReputation = shopReputation;
    }

    public int getShopId() {
        return shopId;
    }

    public int getShopUserId() {
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
