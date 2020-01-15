package com.tokopedia.topads.sdk.view.adapter.factory;


import android.view.ViewGroup;

import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewMore;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public interface BannerAdsTypeFactory {

    int type(BannerShopViewModel viewModel);

    int type(BannerShopProductViewModel viewModel);

    int type(BannerShopViewMore viewModel);

    AbstractViewHolder createViewHolder(ViewGroup view, int viewType);
}
