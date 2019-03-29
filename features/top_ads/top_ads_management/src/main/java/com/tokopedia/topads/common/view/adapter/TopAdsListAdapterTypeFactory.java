package com.tokopedia.topads.common.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.topads.common.view.adapter.viewholder.BaseMultipleCheckViewHolder;
import com.tokopedia.topads.common.view.adapter.viewholder.LoadingViewHolder;
import com.tokopedia.topads.dashboard.view.model.Ad;
import com.tokopedia.topads.common.view.adapter.viewholder.TopAdsAdViewHolder;
import com.tokopedia.topads.common.view.adapter.viewholder.TopAdsErrorNetworkViewHolder;
import com.tokopedia.topads.keyword.view.adapter.viewholder.TopAdsKeywordViewHolder;
import com.tokopedia.topads.keyword.view.model.KeywordAd;

/**
 * Created by hadi.putra on 04/05/18.
 */

public class TopAdsListAdapterTypeFactory<T extends Ad> extends BaseAdapterTypeFactory {

    private BaseMultipleCheckViewHolder.OptionMoreCallback<T> optionMoreCallback;

    public void setOptionMoreCallback(BaseMultipleCheckViewHolder.OptionMoreCallback<T> optionMoreCallback) {
        this.optionMoreCallback = optionMoreCallback;
    }

    @Override
    public int type(LoadingModel viewModel) {
        return LoadingViewHolder.LAYOUT;
    }

    @Override
    public int type(ErrorNetworkModel viewModel) {
        return TopAdsErrorNetworkViewHolder.LAYOUT;
    }

    public int type(T ad){
        if (ad instanceof KeywordAd){
            return TopAdsKeywordViewHolder.LAYOUT;
        } else {
            return TopAdsAdViewHolder.LAYOUT;
        }
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == LoadingViewHolder.LAYOUT) {
            return new LoadingViewHolder(parent);
        } else if (type == TopAdsAdViewHolder.LAYOUT) {
            TopAdsAdViewHolder viewHolder = new TopAdsAdViewHolder(parent);
            viewHolder.setOptionMoreCallback(optionMoreCallback);
            return viewHolder;
        } else if (type == TopAdsKeywordViewHolder.LAYOUT){
            TopAdsKeywordViewHolder topAdsKeywordViewHolder = new TopAdsKeywordViewHolder(parent);
            topAdsKeywordViewHolder.setOptionMoreCallback((BaseMultipleCheckViewHolder.OptionMoreCallback<KeywordAd>) optionMoreCallback);
            return topAdsKeywordViewHolder;
        } else if (type == TopAdsErrorNetworkViewHolder.LAYOUT){
            return new TopAdsErrorNetworkViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
