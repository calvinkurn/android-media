package com.tokopedia.tkpdcontent.feature.profile.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdcontent.feature.profile.view.adapter.viewholder.KolPostViewHolder;
import com.tokopedia.tkpdcontent.feature.profile.view.listener.KolPostListener;
import com.tokopedia.tkpdcontent.feature.profile.view.viewmodel.KolPostViewModel;

/**
 * @author by milhamj on 20/02/18.
 */

public class KolTypeFactoryImpl extends BaseAdapterTypeFactory implements KolTypeFactory {
    private final KolPostListener.View viewListener;

    public KolTypeFactoryImpl(KolPostListener.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public int type(KolPostViewModel emptyFeedBeforeLoginModel) {
        return KolPostViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        AbstractViewHolder abstractViewHolder;
        if (viewType == KolPostViewHolder.LAYOUT)
            abstractViewHolder = new KolPostViewHolder(view, viewListener);
        else
            abstractViewHolder = super.createViewHolder(view, viewType);
        return abstractViewHolder;
    }
}
