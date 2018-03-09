package com.tokopedia.abstraction.base.view.adapter.factory;

import android.support.annotation.CallSuper;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModelShimmeringGrid;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModelShimmeringList;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringListViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingViewholder;


/**
 * @author Kulomady on 1/25/17.
 */

public class BaseAdapterTypeFactory implements AdapterTypeFactory {

    public BaseAdapterTypeFactory() {
    }

    @Override
    public int type(EmptyModel viewModel) {
        return EmptyViewHolder.LAYOUT;
    }

    @Override
    public int type(LoadingModel viewModel) {
        return LoadingViewholder.LAYOUT;
    }

    @Override
    public int type(EmptyResultViewModel viewModel) {
        return EmptyResultViewHolder.LAYOUT;
    }

    @Override
    public int type(ErrorNetworkModel viewModel) {
        return ErrorNetworkViewHolder.LAYOUT;
    }

    @Override
    public int type(LoadingModelShimmeringGrid loadingModelShimmeringGrid) {
        return LoadingShimmeringGridViewHolder.LAYOUT;
    }

    @Override
    public int type(LoadingModelShimmeringList loadingModelShimmeringList) {
        return LoadingShimmeringListViewHolder.LAYOUT;
    }

    @CallSuper
    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder creatViewHolder;
        if (type == LoadingViewholder.LAYOUT) {
            creatViewHolder = new LoadingViewholder(parent);
        } else if (type == LoadingShimmeringGridViewHolder.LAYOUT) {
            creatViewHolder = new LoadingShimmeringGridViewHolder(parent);
        } else if (type == LoadingShimmeringListViewHolder.LAYOUT) {
            creatViewHolder = new LoadingShimmeringListViewHolder(parent);
        } else if (type == ErrorNetworkViewHolder.LAYOUT) {
            creatViewHolder = new ErrorNetworkViewHolder(parent);
        } else if (type == EmptyResultViewHolder.LAYOUT) {
            creatViewHolder = new EmptyResultViewHolder(parent);
        } else if (type == EmptyViewHolder.LAYOUT) {
            creatViewHolder = new EmptyViewHolder(parent);
        } else {
            throw TypeNotSupportedException.create("Layout not supported");
        }
        return creatViewHolder;
    }

}
