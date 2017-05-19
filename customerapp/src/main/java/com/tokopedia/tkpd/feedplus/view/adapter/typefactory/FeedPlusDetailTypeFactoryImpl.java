package com.tokopedia.tkpd.feedplus.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.feedplus.view.FeedPlusDetail;
import com.tokopedia.tkpd.feedplus.view.adapter.viewholder.productcard.EmptyFeedViewHolder;
import com.tokopedia.tkpd.feedplus.view.adapter.viewholder.feeddetail.FeedDetailViewHolder;
import com.tokopedia.tkpd.feedplus.view.fragment.FeedPlusDetailFragment;
import com.tokopedia.tkpd.feedplus.view.viewmodel.FeedDetailViewModel;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedPlusDetailTypeFactoryImpl extends BaseAdapterTypeFactory
        implements FeedPlusDetailTypeFactory  {

    private final FeedPlusDetail.View viewListener;

    public FeedPlusDetailTypeFactoryImpl(FeedPlusDetail.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public int type(FeedDetailViewModel viewModel) {
        return FeedDetailViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;
        switch (type) {
            case FeedDetailViewHolder.LAYOUT:
                viewHolder = new FeedDetailViewHolder(view, viewListener);
                break;
            default:
                viewHolder = super.createViewHolder(view, type);
                break;
        }

        return viewHolder;
    }
}
