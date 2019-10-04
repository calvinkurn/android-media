package com.tokopedia.tkpd.home.wishlist.adapter.factory;

import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.*;

/**
 * Author errysuprayogi on 25,July,2019
 */
public interface WishlistTypeFactory {
    int type(WishlistProductViewModel viewModel);

    int type(WishlistEmptyViewModel viewModel);

    int type(WishlistEmptySearchViewModel viewModel);

    int type(WishlistTopAdsViewModel viewModel);

    int type(WishlistRecomendationViewModel viewModel);

    int type(WishlistRecomTitleViewModel viewModel);
}
