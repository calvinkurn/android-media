package com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.adapter.factory;


import android.view.ViewGroup;

import com.tokopedia.topads.sdk.common.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerProductShimmerUiModel;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopViewMoreUiModel;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopProductUiModel;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopUiModel;

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
