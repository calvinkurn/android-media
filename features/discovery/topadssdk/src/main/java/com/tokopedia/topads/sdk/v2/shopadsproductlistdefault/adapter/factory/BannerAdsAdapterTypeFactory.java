package com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.adapter.factory;

import android.view.ViewGroup;

import com.tokopedia.topads.sdk.common.adapter.exception.TypeNotSupportedException;
import com.tokopedia.topads.sdk.common.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.v2.listener.TopAdsAddToCartClickListener;
import com.tokopedia.topads.sdk.v2.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.v2.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerProductShimmerUiModel;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopViewMoreUiModel;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.viewholder.BannerProductShimmerViewHolder;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.viewholder.BannerShopProductViewHolder;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.viewholder.BannerShopViewHolder;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.viewholder.BannerShowMoreViewHolder;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopProductUiModel;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopUiModel;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerAdsAdapterTypeFactory implements BannerAdsTypeFactory {

    private final TopAdsBannerClickListener topAdsBannerClickListener;
    private final TopAdsItemImpressionListener itemImpressionListener;
    private final TopAdsAddToCartClickListener topAdsAddToCartClickListener;

    public BannerAdsAdapterTypeFactory(TopAdsBannerClickListener topAdsBannerClickListener,
                                       TopAdsItemImpressionListener itemImpressionListener,
                                       TopAdsAddToCartClickListener topAdsAddToCartClickListener) {
        this.topAdsBannerClickListener = topAdsBannerClickListener;
        this.itemImpressionListener = itemImpressionListener;
        this.topAdsAddToCartClickListener = topAdsAddToCartClickListener;
    }

    @Override
    public int type(BannerShopUiModel viewModel) {
        return BannerShopViewHolder.LAYOUT;
    }

    @Override
    public int type(BannerShopProductUiModel viewModel) {
        return BannerShopProductViewHolder.LAYOUT;
    }

    @Override
    public int type(BannerShopViewMoreUiModel viewModel) {
        return BannerShowMoreViewHolder.LAYOUT;
    }

    @Override
    public int type(BannerProductShimmerUiModel viewModel) {
        return BannerProductShimmerViewHolder.Companion.getLAYOUT();
    }

    @Override
    public AbstractViewHolder createViewHolder(ViewGroup view, int viewType) {
        AbstractViewHolder holder;
        if (viewType == BannerShopViewHolder.LAYOUT) {
            holder = new BannerShopViewHolder(view, topAdsBannerClickListener, itemImpressionListener);
        } else if (viewType == BannerShopProductViewHolder.LAYOUT) {
            holder = new BannerShopProductViewHolder(view, topAdsBannerClickListener, itemImpressionListener, topAdsAddToCartClickListener);
        } else if (viewType == BannerShowMoreViewHolder.LAYOUT) {
            holder = new BannerShowMoreViewHolder(view, topAdsBannerClickListener);
        } else if (viewType == BannerProductShimmerViewHolder.Companion.getLAYOUT()) {
            holder = new BannerProductShimmerViewHolder(view);
        } else {
            throw TypeNotSupportedException.create("Layout not supported");
        }
        return holder;
    }
}
