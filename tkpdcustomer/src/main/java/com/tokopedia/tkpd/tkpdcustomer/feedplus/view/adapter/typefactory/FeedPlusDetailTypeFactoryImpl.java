package com.tokopedia.tkpd.tkpdcustomer.feedplus.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.FeedPlusDetail;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.adapter.viewholder.feeddetail.FeedDetailViewHolder;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.viewmodel.FeedDetailViewModel;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedPlusDetailTypeFactoryImpl extends BaseAdapterTypeFactory
        implements FeedPlusDetailTypeFactory {

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

        if (type == FeedDetailViewHolder.LAYOUT)
            viewHolder = new FeedDetailViewHolder(view, viewListener);
        else
            viewHolder = super.createViewHolder(view, type);

        return viewHolder;
    }
}
