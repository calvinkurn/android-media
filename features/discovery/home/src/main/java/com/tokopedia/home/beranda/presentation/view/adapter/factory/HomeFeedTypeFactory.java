package com.tokopedia.home.beranda.presentation.view.adapter.factory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.HomeFeedViewHolder;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel;

public class HomeFeedTypeFactory extends BaseAdapterTypeFactory {
    public int type(HomeFeedViewModel viewModel) {
        return HomeFeedViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == HomeFeedViewHolder.LAYOUT) {
            return new HomeFeedViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }

    @Override
    public int type(LoadingModel viewModel) {
        return LoadingShimmeringGridViewHolder.LAYOUT;
    }
}
