package com.tokopedia.topads.sdk.view.adapter.factory;

import android.view.ViewGroup;

import com.tokopedia.topads.sdk.base.adapter.exception.TypeNotSupportedException;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.listener.TopAdsAddToCartClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerProductShimmerViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerShopProductRevampViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerShopProductViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerShopViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerShowMoreViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerProductShimmerUiModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductUiModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopUiModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewMoreUiModel;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerAdsAdapterTypeFactory implements BannerAdsTypeFactory {

    private final TopAdsBannerClickListener topAdsBannerClickListener;
    private final TopAdsItemImpressionListener itemImpressionListener;
    private final TopAdsAddToCartClickListener topAdsAddToCartClickListener;

    private final Boolean isReimagine;

    public BannerAdsAdapterTypeFactory(TopAdsBannerClickListener topAdsBannerClickListener,
                                       TopAdsItemImpressionListener itemImpressionListener,
                                       TopAdsAddToCartClickListener topAdsAddToCartClickListener,
                                       Boolean isReimagine) {
        this.topAdsBannerClickListener = topAdsBannerClickListener;
        this.itemImpressionListener = itemImpressionListener;
        this.topAdsAddToCartClickListener = topAdsAddToCartClickListener;
        this.isReimagine = isReimagine;
    }

    @Override
    public int type(BannerShopUiModel viewModel) {
        return BannerShopViewHolder.LAYOUT;
    }

    @Override
    public int type(BannerShopProductUiModel viewModel) {
        if(isReimagine) {
            return BannerShopProductRevampViewHolder.LAYOUT;
        } else {
            return BannerShopProductViewHolder.LAYOUT;
        }
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
        } else if(viewType == BannerShopProductRevampViewHolder.LAYOUT){
            holder = new BannerShopProductRevampViewHolder(view, topAdsBannerClickListener, itemImpressionListener);
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
