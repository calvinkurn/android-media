package com.tokopedia.topads.sdk.view.adapter.factory;

import android.view.ViewGroup;

import com.tokopedia.topads.sdk.base.adapter.exception.TypeNotSupportedException;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerShopProductViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerShopViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerShowMoreViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.discovery.ProductGridViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.discovery.ProductListViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewMore;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerAdsAdapterTypeFactory implements BannerAdsTypeFactory {

    private final TopAdsBannerClickListener topAdsBannerClickListener;
    private final TopAdsItemImpressionListener itemImpressionListener;

    public BannerAdsAdapterTypeFactory(TopAdsBannerClickListener topAdsBannerClickListener,
                                       TopAdsItemImpressionListener itemImpressionListener) {
        this.topAdsBannerClickListener = topAdsBannerClickListener;
        this.itemImpressionListener = itemImpressionListener;
    }

    @Override
    public int type(BannerShopViewModel viewModel) {
        return BannerShopViewHolder.LAYOUT;
    }

    @Override
    public int type(BannerShopProductViewModel viewModel) {
        return BannerShopProductViewHolder.LAYOUT;
    }

    @Override
    public int type(BannerShopViewMore viewModel) {
        return BannerShowMoreViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(ViewGroup view, int viewType) {
        AbstractViewHolder holder;
        if (viewType == BannerShopViewHolder.LAYOUT) {
            holder = new BannerShopViewHolder(view, topAdsBannerClickListener, itemImpressionListener);
        } else if (viewType == BannerShopProductViewHolder.LAYOUT) {
            holder = new BannerShopProductViewHolder(view, topAdsBannerClickListener, itemImpressionListener);
        } else if (viewType == BannerShowMoreViewHolder.LAYOUT) {
            holder = new BannerShowMoreViewHolder(view);
        } else {
            throw TypeNotSupportedException.create("Layout not supported");
        }
        return holder;
    }
}
