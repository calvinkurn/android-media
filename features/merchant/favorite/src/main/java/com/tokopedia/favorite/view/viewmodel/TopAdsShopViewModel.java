package com.tokopedia.favorite.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.favorite.view.adapter.FavoriteTypeFactory;

import java.util.Collections;
import java.util.List;

/**
 * @author kulomady on 1/24/17.
 */

public class TopAdsShopViewModel implements Visitable<FavoriteTypeFactory> {

    private List<TopAdsShopItem> adsShopItems;

    @Override
    public int type(FavoriteTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }

    public List<TopAdsShopItem> getAdsShopItems() {
        if (adsShopItems == null) {
            return Collections.emptyList();
        }
        return adsShopItems;
    }

    public void setAdsShopItems(List<TopAdsShopItem> adsShopItems) {
        this.adsShopItems = adsShopItems;
    }
}
