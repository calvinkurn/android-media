package com.tokopedia.core.base.adapter;

import android.support.annotation.CallSuper;
import android.view.View;

import com.tokopedia.core.base.adapter.exception.TypeNotSupportedException;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.model.ErrorNetworkModel;
import com.tokopedia.core.base.adapter.model.LoadingModel;
import com.tokopedia.core.base.adapter.model.RetryModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.base.adapter.viewholders.EmptyViewHolder;
import com.tokopedia.core.base.adapter.viewholders.ErrorNetworkViewHolder;
import com.tokopedia.core.base.adapter.viewholders.LoadingViewholder;


/**
 * @author Kulomady on 1/25/17.
 */

/**
 * Use BaseAdapter (visitable pattern) from tkpd abstraction
 */
@Deprecated
public class BaseAdapterTypeFactory implements AdapterTypeFactory {

    @Override
    public int type(EmptyModel viewModel) {
        return EmptyViewHolder.LAYOUT;
    }

    @Override
    public int type(LoadingModel viewModel) {
        return LoadingViewholder.LAYOUT;
    }

    @Override
    public int type(ErrorNetworkModel viewModel) {
        return ErrorNetworkViewHolder.LAYOUT;
    }

    @CallSuper
    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder creatViewHolder;
        if (type == EmptyViewHolder.LAYOUT) {
            creatViewHolder = new EmptyViewHolder(parent);
        } else if (type == LoadingViewholder.LAYOUT) {
            creatViewHolder = new LoadingViewholder(parent);
        } else if (type == ErrorNetworkViewHolder.LAYOUT) {
            creatViewHolder = new ErrorNetworkViewHolder(parent);
        } else {
            throw TypeNotSupportedException.create("Layout not supported");
        }
        return creatViewHolder;
    }

    @Override
    public int type(RetryModel retryModel) {
        return 0;
    }


}
