package com.tokopedia.feedplus.domain.model.feeddetail;

import javax.annotation.Nullable;

/**
 * @author by nisie on 5/24/17.
 */

public class FeedDetailSourceDomain {

    private final @Nullable
    Integer type;

    private final @Nullable
    FeedDetailShopDomain shop;

    public FeedDetailSourceDomain(Integer type, FeedDetailShopDomain shop) {
        this.type = type;
        this.shop = shop;
    }

    @Nullable
    public Integer getType() {
        return type;
    }

    @Nullable
    public FeedDetailShopDomain getShop() {
        return shop;
    }
}
