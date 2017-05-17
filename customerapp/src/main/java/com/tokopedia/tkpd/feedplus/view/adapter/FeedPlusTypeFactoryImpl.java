package com.tokopedia.tkpd.feedplus.view.adapter;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.feedplus.FeedPlus;
import com.tokopedia.tkpd.feedplus.view.adapter.viewholder.EmptyFeedViewHolder;
import com.tokopedia.tkpd.feedplus.view.adapter.viewholder.productcard.ProductCardViewHolder;
import com.tokopedia.tkpd.feedplus.view.viewmodel.ProductCardViewModel;

/**
 * @author by nisie on 5/15/17.
 */

public class FeedPlusTypeFactoryImpl extends BaseAdapterTypeFactory implements FeedPlusTypeFactory {

    private final FeedPlus.View viewListener;

    public FeedPlusTypeFactoryImpl(FeedPlus.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public int type(ProductCardViewModel productCardViewModel) {
        return ProductCardViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;
        switch (type) {
            case EmptyFeedViewHolder.LAYOUT:
                viewHolder = new EmptyFeedViewHolder(view);
                break;
            case ProductCardViewHolder.LAYOUT:
                viewHolder = new ProductCardViewHolder(view, viewListener);
                break;
            default:
                viewHolder = super.createViewHolder(view, type);
                break;
        }

        return viewHolder;
    }
}
