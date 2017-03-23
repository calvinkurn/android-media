package com.tokopedia.tkpd.home.favorite.domain.model;

/**
 * Created by erry on 31/01/17.
 */

public class FavShop {

    private boolean isValid;
    private String message;
    private FavoriteShopItem favoriteShopItem;

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        this.isValid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FavoriteShopItem getFavoriteShopItem() {
        return favoriteShopItem;
    }

    public void setFavoriteShopItem(FavoriteShopItem favoriteShopItem) {
        this.favoriteShopItem = favoriteShopItem;
    }
}
