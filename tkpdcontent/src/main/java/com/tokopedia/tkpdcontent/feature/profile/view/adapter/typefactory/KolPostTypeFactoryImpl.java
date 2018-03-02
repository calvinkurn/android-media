package com.tokopedia.tkpdcontent.feature.profile.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdcontent.feature.profile.view.adapter.viewholder.EmptyKolPostViewHolder;
import com.tokopedia.tkpdcontent.feature.profile.view.adapter.viewholder.KolPostViewHolder;
import com.tokopedia.tkpdcontent.feature.profile.view.listener.KolPostListener;
import com.tokopedia.tkpdcontent.feature.profile.view.viewmodel.EmptyKolPostViewModel;
import com.tokopedia.tkpdcontent.feature.profile.view.viewmodel.KolPostViewModel;

/**
 * @author by milhamj on 20/02/18.
 */

public class KolPostTypeFactoryImpl extends BaseAdapterTypeFactory implements KolPostTypeFactory {
    private final KolPostListener.View viewListener;

    public KolPostTypeFactoryImpl(KolPostListener.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public int type(KolPostViewModel kolPostViewModel) {
        return KolPostViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptyKolPostViewModel emptyKolPostViewModel) {
        return EmptyKolPostViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        AbstractViewHolder abstractViewHolder;
        if (viewType == KolPostViewHolder.LAYOUT)
            abstractViewHolder = new KolPostViewHolder(view, viewListener);
        else if (viewType == EmptyKolPostViewHolder.LAYOUT)
            abstractViewHolder = new EmptyKolPostViewHolder(view);
        else
            abstractViewHolder = super.createViewHolder(view, viewType);
        return abstractViewHolder;
    }
}
