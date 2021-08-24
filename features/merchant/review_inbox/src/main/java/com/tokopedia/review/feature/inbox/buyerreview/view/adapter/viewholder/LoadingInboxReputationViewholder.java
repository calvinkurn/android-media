package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import androidx.annotation.LayoutRes;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.review.inbox.R;

public class LoadingInboxReputationViewholder extends AbstractViewHolder<LoadingModel> {
    @LayoutRes
    public final static int LAYOUT = R.layout.item_shimmering_inbox_reputation;

    public LoadingInboxReputationViewholder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(LoadingModel element) {
        itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

}
