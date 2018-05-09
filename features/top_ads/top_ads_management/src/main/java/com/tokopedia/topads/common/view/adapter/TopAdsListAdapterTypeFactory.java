package com.tokopedia.topads.common.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.topads.common.view.adapter.viewholder.LoadingViewHolder;
import com.tokopedia.topads.dashboard.view.model.Ad;
import com.tokopedia.topads.common.view.adapter.viewholder.TopAdsAdViewHolder;
import com.tokopedia.topads.common.view.adapter.viewholder.TopAdsErrorNetworkViewHolder;

/**
 * Created by hadi.putra on 04/05/18.
 */

public class TopAdsListAdapterTypeFactory<T extends Ad> extends BaseAdapterTypeFactory {

    @Override
    public int type(LoadingModel viewModel) {
        return LoadingViewHolder.LAYOUT;
    }

    @Override
    public int type(ErrorNetworkModel viewModel) {
        return TopAdsErrorNetworkViewHolder.LAYOUT;
    }

    public int type(T ad){
        return TopAdsAdViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == LoadingViewHolder.LAYOUT) {
            return new LoadingViewHolder(parent);
        } else if (type == TopAdsAdViewHolder.LAYOUT) {
            return new TopAdsAdViewHolder(parent);
        } else if (type == TopAdsErrorNetworkViewHolder.LAYOUT){
            return new TopAdsErrorNetworkViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
