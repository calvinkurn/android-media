package com.tokopedia.kol.feature.post.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.kol.feature.post.view.adapter.viewholder.EmptyKolPostViewHolder;
import com.tokopedia.kol.feature.post.view.adapter.viewholder.ExploreViewHolder;
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostViewHolder;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.viewmodel.EmptyKolPostViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.ExploreViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;

/**
 * @author by milhamj on 20/02/18.
 */

public class KolPostTypeFactoryImpl extends BaseAdapterTypeFactory implements KolPostTypeFactory {
    private final KolPostListener.View.ViewHolder viewListener;

    public KolPostTypeFactoryImpl(KolPostListener.View.ViewHolder viewListener) {
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
    public int type(ExploreViewModel exploreViewModel) {
        return ExploreViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        AbstractViewHolder abstractViewHolder;
        if (viewType == KolPostViewHolder.LAYOUT) {
            abstractViewHolder = new KolPostViewHolder(view,
                    viewListener,
                    KolPostViewHolder.Type.PROFILE);
        }
        else if (viewType == EmptyKolPostViewHolder.LAYOUT)
            abstractViewHolder = new EmptyKolPostViewHolder(view);
        else if (viewType == ExploreViewHolder.LAYOUT)
            abstractViewHolder = new ExploreViewHolder(view);
        else
            abstractViewHolder = super.createViewHolder(view, viewType);
        return abstractViewHolder;
    }
}
