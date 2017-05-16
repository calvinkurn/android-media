package com.tokopedia.tkpd.feedplus.view.adapter;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.feedplus.FeedPlus;
import com.tokopedia.tkpd.feedplus.view.adapter.viewholder.DoubleProductCardViewHolder;
import com.tokopedia.tkpd.feedplus.view.adapter.viewholder.EmptyFeedViewHolder;
import com.tokopedia.tkpd.feedplus.view.adapter.viewholder.SingleProductViewHolder;
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
        switch (productCardViewModel.getListProduct().size()) {
            case 0:
                return EmptyFeedViewHolder.LAYOUT;
            case 1:
                return SingleProductViewHolder.LAYOUT;
            case 2:
                return DoubleProductCardViewHolder.LAYOUT;
            case 3:
                return EmptyFeedViewHolder.LAYOUT;
            case 4:
                return DoubleProductCardViewHolder.LAYOUT;
            case 5:
                return EmptyFeedViewHolder.LAYOUT;
            default:
                return EmptyFeedViewHolder.LAYOUT;
        }
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder;
        switch (type) {
            case EmptyFeedViewHolder.LAYOUT:
                viewHolder = new EmptyFeedViewHolder(parent);
                break;
            case SingleProductViewHolder.LAYOUT:
                viewHolder = new SingleProductViewHolder(parent, viewListener);
                break;
            case DoubleProductCardViewHolder.LAYOUT:
                viewHolder = new DoubleProductCardViewHolder(parent, viewListener);
                break;
            default:
                viewHolder = super.createViewHolder(parent, type);
                break;
        }


        return viewHolder;
    }
}
