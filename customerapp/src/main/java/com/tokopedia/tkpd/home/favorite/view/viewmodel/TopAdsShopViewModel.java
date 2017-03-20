package com.tokopedia.tkpd.home.favorite.view.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.home.favorite.view.adapter.FavoriteTypeFactory;

/**
 * @author kulomady on 1/24/17.
 */

public class TopAdsShopViewModel implements Visitable<FavoriteTypeFactory> {
    @Override
    public int type(FavoriteTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }
}
