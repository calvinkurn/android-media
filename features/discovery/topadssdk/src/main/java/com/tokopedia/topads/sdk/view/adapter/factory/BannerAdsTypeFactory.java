package com.tokopedia.topads.sdk.view.adapter.factory;


import android.view.ViewGroup;

import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerProductShimmerUiModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductUiModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopUiModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewMoreUiModel;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public interface BannerAdsTypeFactory {

    int type(BannerShopUiModel viewModel);

    int type(BannerShopProductUiModel viewModel);

    int type(BannerShopViewMoreUiModel viewModel);

    int type(BannerProductShimmerUiModel viewModel);

    AbstractViewHolder createViewHolder(ViewGroup view, int viewType);
}
