package com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.adapter.factory;

import android.view.ViewGroup;

import com.tokopedia.topads.sdk.common.adapter.exception.TypeNotSupportedException;
import com.tokopedia.topads.sdk.common.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.v2.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.v2.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.adapter.factory.BannerAdsTypeFactory;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.viewholder.BannerProductShimmerViewHolder;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.viewholder.BannerShopProductReimagineViewHolder;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.viewholder.BannerShopViewHolder;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.viewholder.BannerShowMoreReimagineViewHolder;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerProductShimmerUiModel;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopProductUiModel;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopUiModel;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopViewMoreUiModel;

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
            holder = new BannerShowMoreReimagineViewHolder(view, itemImpressionListener, topAdsBannerClickListener);
        } else if (viewType == BannerProductShimmerViewHolder.Companion.getLAYOUT()) {
            holder = new BannerProductShimmerViewHolder(view);
        } else {
            throw TypeNotSupportedException.create("Layout not supported");
        }
        return holder;
    }
}
