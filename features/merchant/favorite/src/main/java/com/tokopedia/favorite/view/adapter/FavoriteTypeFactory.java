package com.tokopedia.favorite.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.favorite.view.viewmodel.FavoriteShopViewModel;
import com.tokopedia.favorite.view.viewmodel.TopAdsShopViewModel;

/**
 * @author kulomady on 1/24/17.
 */

public interface FavoriteTypeFactory extends AdapterTypeFactory {

    int type(TopAdsShopViewModel viewModel);

    int type(FavoriteShopViewModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);

}
