package com.tokopedia.tkpd.feedplus.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.feedplus.FeedPlus;
import com.tokopedia.tkpd.feedplus.view.adapter.viewholder.productcard.DoubleProductCardViewHolder;
import com.tokopedia.tkpd.feedplus.view.adapter.viewholder.EmptyFeedViewHolder;
import com.tokopedia.tkpd.feedplus.view.adapter.viewholder.productcard.SingleProductViewHolder;
import com.tokopedia.tkpd.feedplus.view.adapter.viewholder.productcard.TripleProductCardViewHolder;
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
                return DoubleProductCardViewHolder.IDENTIFIER;
            case 3:
                return TripleProductCardViewHolder.LAYOUT;
            case 4:
                return DoubleProductCardViewHolder.LAYOUT;
            case 5:
                return EmptyFeedViewHolder.LAYOUT;
            default:
                return EmptyFeedViewHolder.LAYOUT;
        }
    }

    @Override
    public AbstractViewHolder createViewHolder(ViewGroup parent, int type) {
        Context context = parent.getContext();
        View view = parent;
        AbstractViewHolder viewHolder;

        if (type != DoubleProductCardViewHolder.IDENTIFIER)
            view = LayoutInflater.from(context).inflate(type, parent, false);

        switch (type) {
            case EmptyFeedViewHolder.LAYOUT:
                viewHolder = new EmptyFeedViewHolder(view);
                break;
            case SingleProductViewHolder.LAYOUT:
                viewHolder = new SingleProductViewHolder(view, viewListener);
                break;
            case DoubleProductCardViewHolder.IDENTIFIER:
                int layoutId = DoubleProductCardViewHolder.LAYOUT;
                view = LayoutInflater.from(context).inflate(layoutId, parent, false);
                viewHolder = new DoubleProductCardViewHolder(view, viewListener);
                break;
            case TripleProductCardViewHolder.LAYOUT:
                viewHolder = new TripleProductCardViewHolder(view, viewListener);
                break;
            default:
                viewHolder = super.createViewHolder(view, type);
                break;
        }

        return viewHolder;
    }
}
