package com.tokopedia.topads.product.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.product.view.adapter.viewholder.LoadingViewHolder;
import com.tokopedia.topads.product.view.adapter.viewholder.TopAdsAdViewHolder;

/**
 * Created by hadi.putra on 04/05/18.
 */

public class TopAdsProductAdListAdapterTypeFactory extends BaseAdapterTypeFactory {

    @Override
    public int type(LoadingModel viewModel) {
        return LoadingViewHolder.LAYOUT;
    }

    public int type(ProductAd productAd){
        return TopAdsAdViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == LoadingViewHolder.LAYOUT) {
            return new LoadingViewHolder(parent);
        } else if (type == TopAdsAdViewHolder.LAYOUT){
            return new TopAdsAdViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
