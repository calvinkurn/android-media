package com.tokopedia.tkpd.home.favorite.view.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.home.favorite.view.adapter.FavoriteTypeFactory;

/**
 *
 * @author kulomady on 1/24/17.
 */

public class FavoriteShopViewModel implements Visitable<FavoriteTypeFactory> {
    private String shopId;
    private String shopAvatarImageUrl;
    private String shopName;
    private String shopLocation;
    private boolean isFavoriteShop;
    private String badgeUrl;

    @Override
    public int type(FavoriteTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }

    public String getShopAvatarImageUrl() {
        return shopAvatarImageUrl;
    }

    public void setShopAvatarImageUrl(String shopAvatarImageUrl) {
        this.shopAvatarImageUrl = shopAvatarImageUrl;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public boolean isFavoriteShop() {
        return isFavoriteShop;
    }

    public void setFavoriteShop(boolean favoriteShop) {
        isFavoriteShop = favoriteShop;
    }

    public String getBadgeUrl() {
        return badgeUrl;
    }

    public void setBadgeUrl(String badgeUrl) {
        this.badgeUrl = badgeUrl;
    }
}
