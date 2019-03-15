package com.tokopedia.navigation.presentation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.navigation.domain.model.Inbox;
import com.tokopedia.navigation.domain.model.RecomTitle;
import com.tokopedia.navigation.domain.model.Recomendation;
import com.tokopedia.navigation.presentation.adapter.viewholder.InboxViewHolder;
import com.tokopedia.navigation.presentation.adapter.viewholder.RecomTitleViewHolder;
import com.tokopedia.navigation.presentation.adapter.viewholder.RecomendationViewHolder;

/**
 * Author errysuprayogi on 13,March,2019
 */
public class InboxAdapterTypeFactory extends BaseAdapterTypeFactory implements InboxTypeFactory {

    private InboxAdapterListener listener;

    public InboxAdapterTypeFactory(InboxAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public int type(Inbox inbox) {
        return InboxViewHolder.LAYOUT;
    }

    @Override
    public int type(Recomendation recomendation) {
        return RecomendationViewHolder.LAYOUT;
    }

    @Override
    public int type(RecomTitle recomTitle) {
        return RecomTitleViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {
        AbstractViewHolder viewHolder;
        if (type == InboxViewHolder.LAYOUT)
            viewHolder = new InboxViewHolder(view, listener);
        else if (type == RecomendationViewHolder.LAYOUT)
            viewHolder = new RecomendationViewHolder(view, listener);
        else if (type == RecomTitleViewHolder.LAYOUT)
            viewHolder = new RecomTitleViewHolder(view);
        else viewHolder = super.createViewHolder(view, type);
        return viewHolder;
    }
}
