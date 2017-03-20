package com.tokopedia.tkpd.home.favorite.domain.model;

/**
 * Created by erry on 31/01/17.
 */

public class FavShop {

    private boolean mIsValid;
    private String mMessage;
    private FavoriteShopItem favoriteShopItem;

    public boolean ismIsValid() {
        return mIsValid;
    }

    public void setmIsValid(boolean mIsValid) {
        this.mIsValid = mIsValid;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public FavoriteShopItem getFavoriteShopItem() {
        return favoriteShopItem;
    }

    public void setFavoriteShopItem(FavoriteShopItem favoriteShopItem) {
        this.favoriteShopItem = favoriteShopItem;
    }
}
