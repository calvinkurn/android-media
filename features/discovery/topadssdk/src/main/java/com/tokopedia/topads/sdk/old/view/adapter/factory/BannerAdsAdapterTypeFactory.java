package com.tokopedia.topads.sdk.old.view.adapter.factory;

import android.view.ViewGroup;

import com.tokopedia.topads.sdk.common.adapter.exception.TypeNotSupportedException;
import com.tokopedia.topads.sdk.common.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.old.listener.TopAdsAddToCartClickListener;
import com.tokopedia.topads.sdk.old.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.old.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.old.view.adapter.viewholder.banner.BannerProductShimmerViewHolder;
import com.tokopedia.topads.sdk.old.view.adapter.viewholder.banner.BannerShopViewHolder;
import com.tokopedia.topads.sdk.old.view.adapter.viewholder.banner.BannerShopProductViewHolder;
import com.tokopedia.topads.sdk.old.view.adapter.viewholder.banner.BannerShowMoreViewHolder;
import com.tokopedia.topads.sdk.old.view.adapter.viewmodel.banner.BannerProductShimmerUiModel;
import com.tokopedia.topads.sdk.old.view.adapter.viewmodel.banner.BannerShopProductUiModel;
import com.tokopedia.topads.sdk.old.view.adapter.viewmodel.banner.BannerShopUiModel;
import com.tokopedia.topads.sdk.old.view.adapter.viewmodel.banner.BannerShopViewMoreUiModel;

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
