package com.tokopedia.explore.view.adapter.factory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.explore.view.adapter.viewholder.ExploreImageViewHolder;
import com.tokopedia.explore.view.listener.ContentExploreContract;
import com.tokopedia.explore.view.uimodel.ExploreImageViewModel;

/**
 * @author by milhamj on 20/02/18.
 */

public class ExploreImageTypeFactoryImpl extends BaseAdapterTypeFactory
        implements ExploreImageTypeFactory {

    private final ContentExploreContract.View viewListener;

    public ExploreImageTypeFactoryImpl(ContentExploreContract.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public int type(ExploreImageViewModel viewModel) {
        return ExploreImageViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        AbstractViewHolder abstractViewHolder;
        if (viewType == ExploreImageViewHolder.LAYOUT)
            abstractViewHolder = new ExploreImageViewHolder(view, viewListener);
        else
            abstractViewHolder = super.createViewHolder(view, viewType);
        return abstractViewHolder;
    }
}
