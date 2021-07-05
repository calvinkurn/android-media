package com.tokopedia.recentview.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.recentview.view.listener.RecentView;
import com.tokopedia.recentview.view.adapter.viewholder.RecentViewDetailProductViewHolder;
import com.tokopedia.recentview.view.viewmodel.RecentViewDetailProductDataModel;
import com.tokopedia.recentview.view.viewmodel.RecentViewProductDataModel;

/**
 * @author by nisie on 7/4/17.
 */

public class RecentViewTypeFactoryImpl extends BaseAdapterTypeFactory
        implements RecentViewTypeFactory {

    private final RecentView.View viewListener;

    public RecentViewTypeFactoryImpl(RecentView.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public int type(RecentViewDetailProductDataModel viewModel) {
        return RecentViewDetailProductViewHolder.LAYOUT;
    }

    @Override
    public int type(RecentViewProductDataModel viewModel) {
        return -1;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;

        if (type == RecentViewDetailProductViewHolder.LAYOUT)
            viewHolder = new RecentViewDetailProductViewHolder(view, viewListener);
        else viewHolder = super.createViewHolder(view, type);
        return viewHolder;
    }


}
