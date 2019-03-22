package com.tokopedia.home.beranda.presentation.view.adapter.factory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.HomeFeedLoadingMoreViewHolder;
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedContract;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.HomeFeedViewHolder;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel;

public class HomeFeedTypeFactory extends BaseAdapterTypeFactory {

    private final HomeFeedContract.View view;

    public HomeFeedTypeFactory(HomeFeedContract.View view) {
        this.view = view;
    }

    public int type(HomeFeedViewModel viewModel) {
        return HomeFeedViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == HomeFeedViewHolder.LAYOUT) {
            return new HomeFeedViewHolder(parent, view);
        } else if (type == LoadingShimmeringGridViewHolder.LAYOUT) {
            return new LoadingShimmeringGridViewHolder(parent);
        } else if (type == HomeFeedLoadingMoreViewHolder.LAYOUT) {
            return new HomeFeedLoadingMoreViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }

    @Override
    public int type(LoadingModel viewModel) {
        return LoadingShimmeringGridViewHolder.LAYOUT;
    }

    @Override
    public int type(LoadingMoreModel viewModel) {
        return HomeFeedLoadingMoreViewHolder.LAYOUT;
    }
}
