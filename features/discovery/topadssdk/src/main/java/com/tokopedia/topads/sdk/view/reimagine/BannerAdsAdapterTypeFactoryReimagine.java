package com.tokopedia.topads.sdk.view.reimagine;

import android.view.ViewGroup;

import com.tokopedia.topads.sdk.base.adapter.exception.TypeNotSupportedException;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.listener.TopAdsAddToCartClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.view.adapter.factory.BannerAdsTypeFactory;
import com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerProductShimmerViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerShopProductReimagineViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerShopProductViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerShopViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerShowMoreReimagineViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerShowMoreViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerProductShimmerUiModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductUiModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopUiModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewMoreUiModel;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerAdsAdapterTypeFactoryReimagine implements BannerAdsTypeFactory {

    private final TopAdsBannerClickListener topAdsBannerClickListener;
    private final TopAdsItemImpressionListener itemImpressionListener;

    public BannerAdsAdapterTypeFactoryReimagine(TopAdsBannerClickListener topAdsBannerClickListener,
                                                TopAdsItemImpressionListener itemImpressionListener) {
        this.topAdsBannerClickListener = topAdsBannerClickListener;
        this.itemImpressionListener = itemImpressionListener;
    }

    @Override
    public int type(BannerShopUiModel viewModel) {
        return BannerShopViewHolder.LAYOUT;
    }

    @Override
    public int type(BannerShopProductUiModel viewModel) {
            return BannerShopProductReimagineViewHolder.LAYOUT;
    }

    @Override
    public int type(BannerShopViewMoreUiModel viewModel) {
            return BannerShowMoreReimagineViewHolder.LAYOUT;
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
        }  else if(viewType == BannerShopProductReimagineViewHolder.LAYOUT) {
            holder = new BannerShopProductReimagineViewHolder(view, topAdsBannerClickListener, itemImpressionListener);
        } else if(viewType == BannerShowMoreReimagineViewHolder.LAYOUT){
            holder = new BannerShowMoreReimagineViewHolder(view, topAdsBannerClickListener);
        } else if (viewType == BannerProductShimmerViewHolder.Companion.getLAYOUT()) {
            holder = new BannerProductShimmerViewHolder(view);
        } else {
            throw TypeNotSupportedException.create("Layout not supported");
        }
        return holder;
    }
}
