package com.tokopedia.tkpd.home.favorite.view.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.home.favorite.view.adapter.FavoriteTypeFactory;

import java.util.Collections;
import java.util.List;

/**
 * @author kulomady on 1/24/17.
 */

public class WishlistViewModel implements Visitable<FavoriteTypeFactory> {
    private List<WishlistItem> wishlistItems;

    @Override
    public int type(FavoriteTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }

    public List<WishlistItem> getWishlistItems() {
        if (wishlistItems == null) {
            return Collections.emptyList();
        }
        return wishlistItems;
    }

    public void setWishlistItems(List<WishlistItem> wishlistItems) {
        this.wishlistItems = wishlistItems;
    }
}
