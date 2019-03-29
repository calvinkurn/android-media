package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;

public class HomeFeedLoadingMoreViewHolder extends AbstractViewHolder<LoadingMoreModel> {
    @LayoutRes
    public final static int LAYOUT = R.layout.loading_layout;

    public HomeFeedLoadingMoreViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(LoadingMoreModel element) {

    }
}