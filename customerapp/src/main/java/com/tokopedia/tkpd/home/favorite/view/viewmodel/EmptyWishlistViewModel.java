package com.tokopedia.tkpd.home.favorite.view.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.home.favorite.view.adapter.FavoriteTypeFactory;

/**
 * @author madi on 4/3/17.
 */

public class EmptyWishlistViewModel implements Visitable<FavoriteTypeFactory> {

    @Override
    public int type(FavoriteTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }
}
