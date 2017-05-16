package com.tokopedia.tkpd.feedplus.view.adapter;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.feedplus.view.adapter.viewholder.EmptyFeedViewHolder;
import com.tokopedia.tkpd.feedplus.view.adapter.viewholder.OneProductViewHolder;
import com.tokopedia.tkpd.feedplus.view.viewmodel.ProductCardViewModel;

/**
 * @author by nisie on 5/15/17.
 */

public class FeedPlusTypeFactoryImpl extends BaseAdapterTypeFactory implements FeedPlusTypeFactory {

    public FeedPlusTypeFactoryImpl() {

    }

    @Override
    public int type(ProductCardViewModel productCardViewModel) {
        switch (productCardViewModel.getListProduct().size()) {
            case 0:
                return EmptyFeedViewHolder.LAYOUT;
            case 1:
                return OneProductViewHolder.LAYOUT;
            case 2:
                return EmptyFeedViewHolder.LAYOUT;
            case 3:
                return EmptyFeedViewHolder.LAYOUT;
            case 4:
                return EmptyFeedViewHolder.LAYOUT;
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
            case OneProductViewHolder.LAYOUT:
                viewHolder = new OneProductViewHolder(parent);
                break;
            default:
                viewHolder = super.createViewHolder(parent, type);
                break;
        }


        return viewHolder;
    }
}
