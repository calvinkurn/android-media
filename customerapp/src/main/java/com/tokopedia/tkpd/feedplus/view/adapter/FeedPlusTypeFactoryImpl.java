package com.tokopedia.tkpd.feedplus.view.adapter;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.feedplus.view.adapter.viewholder.EmptyFeedViewHolder;
import com.tokopedia.tkpd.feedplus.view.viewmodel.ProductCardViewModel;

/**
 * @author by nisie on 5/15/17.
 */

public class FeedPlusTypeFactoryImpl extends BaseAdapterTypeFactory implements FeedPlusTypeFactory {

    public FeedPlusTypeFactoryImpl() {

    }

    @Override
    public int type(ProductCardViewModel productCardViewModel) {
        return EmptyFeedViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder;
        switch (type) {
            case EmptyFeedViewHolder.LAYOUT:
                viewHolder = new EmptyFeedViewHolder(parent);
                break;
            default:
                viewHolder = super.createViewHolder(parent, type);
                break;
        }


        return viewHolder;
    }
}
