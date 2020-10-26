package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.review.R;

public class LoadingInboxReputationDetailViewholder extends AbstractViewHolder<LoadingModel> {
    @LayoutRes
    public final static int LAYOUT = R.layout.item_shimmering_inbox_detail_reputation;

    public LoadingInboxReputationDetailViewholder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(LoadingModel element) {
        itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

}
