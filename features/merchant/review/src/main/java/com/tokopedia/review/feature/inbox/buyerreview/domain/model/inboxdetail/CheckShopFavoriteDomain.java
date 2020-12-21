package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail;

/**
 * @author by nisie on 9/26/17.
 */

public class CheckShopFavoriteDomain {
    boolean isShopFavorited;

    public CheckShopFavoriteDomain(boolean isShopFavorited) {
        this.isShopFavorited = isShopFavorited;
    }

    public boolean isShopFavorited() {
        return isShopFavorited;
    }
}
