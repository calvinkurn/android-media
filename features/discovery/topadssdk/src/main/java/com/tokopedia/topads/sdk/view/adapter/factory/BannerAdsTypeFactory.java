package com.tokopedia.topads.sdk.view.adapter.factory;


import android.view.ViewGroup;

import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerProductShimmerViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewMoreModel;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public interface BannerAdsTypeFactory {

    int type(BannerShopViewModel viewModel);

    int type(BannerShopProductViewModel viewModel);

    int type(BannerShopViewMoreModel viewModel);

    int type(BannerProductShimmerViewModel viewModel);

    AbstractViewHolder createViewHolder(ViewGroup view, int viewType);
}
