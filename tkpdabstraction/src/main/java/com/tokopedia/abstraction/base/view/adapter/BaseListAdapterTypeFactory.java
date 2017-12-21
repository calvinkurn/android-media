package com.tokopedia.abstraction.base.view.adapter;

import android.support.annotation.CallSuper;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

/**
 * @author by alvarisi on 12/8/17.
 */

public abstract class BaseListAdapterTypeFactory<T extends Visitable> extends BaseAdapterTypeFactory implements BaseListTypeFactory<T> {

    public BaseListAdapterTypeFactory() {

    }

    @Override
    public abstract int type(T viewModel);

    @CallSuper
    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        return super.createViewHolder(parent, type);
    }
}
