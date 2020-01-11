package com.tokopedia.baselist.adapter.factory;

import androidx.annotation.CallSuper;
import android.view.View;

import com.tokopedia.baselist.adapter.exception.TypeNotSupportedException;
import com.tokopedia.baselist.adapter.model.EmptyModel;
import com.tokopedia.baselist.adapter.model.EmptyResultViewModel;
import com.tokopedia.baselist.adapter.model.ErrorNetworkModel;
import com.tokopedia.baselist.adapter.model.LoadingModel;
import com.tokopedia.baselist.adapter.model.LoadingMoreModel;
import com.tokopedia.baselist.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.baselist.adapter.viewholders.EmptyResultViewHolder;
import com.tokopedia.baselist.adapter.viewholders.EmptyViewHolder;
import com.tokopedia.baselist.adapter.viewholders.ErrorNetworkViewHolder;
import com.tokopedia.baselist.adapter.viewholders.HideViewHolder;
import com.tokopedia.baselist.adapter.viewholders.LoadingMoreViewHolder;
import com.tokopedia.baselist.adapter.viewholders.LoadingViewholder;


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
    public int type(EmptyResultViewModel viewModel) {
        return EmptyResultViewHolder.LAYOUT;
    }

    @Override
    public int type(ErrorNetworkModel viewModel) {
        return ErrorNetworkViewHolder.LAYOUT;
    }

    @Override
    public int type(LoadingModel viewModel) {
        return LoadingViewholder.LAYOUT;
    }

    @Override
    public int type(LoadingMoreModel viewModel) {
        return LoadingMoreViewHolder.LAYOUT;
    }

    @CallSuper
    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder creatViewHolder;
        if (type == LoadingMoreViewHolder.LAYOUT) {
            creatViewHolder = new LoadingMoreViewHolder(parent);
        } else if (type == LoadingViewholder.LAYOUT) {
            creatViewHolder = new LoadingViewholder(parent);
        } else if (type == ErrorNetworkViewHolder.LAYOUT) {
            creatViewHolder = new ErrorNetworkViewHolder(parent);
        } else if (type == EmptyResultViewHolder.LAYOUT) {
            creatViewHolder = new EmptyResultViewHolder(parent);
        } else if (type == EmptyViewHolder.LAYOUT) {
            creatViewHolder = new EmptyViewHolder(parent);
        } else if (type == HideViewHolder.LAYOUT){
            creatViewHolder = new HideViewHolder(parent);
        } else {
            throw TypeNotSupportedException.create("Layout not supported");
        }
        return creatViewHolder;
    }

}
