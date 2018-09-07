package com.tokopedia.tkpd.home.favorite.view.adapter;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.FavoriteShopViewModel;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.TopAdsShopViewModel;

/**
 * @author kulomady on 1/24/17.
 */

public interface FavoriteTypeFactory {

    int type(TopAdsShopViewModel viewModel);

    int type(FavoriteShopViewModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);

}
