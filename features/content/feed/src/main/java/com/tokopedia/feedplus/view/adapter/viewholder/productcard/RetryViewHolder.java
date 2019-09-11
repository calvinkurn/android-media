package com.tokopedia.feedplus.view.adapter.viewholder.productcard;

import androidx.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.viewmodel.RetryModel;

/**
 * Created by stevenfredian on 5/31/17.
 */

public class RetryViewHolder extends AbstractViewHolder<RetryModel> {
    @LayoutRes
    public final static int LAYOUT = R.layout.retry_layout;
    private final View button;
    private final FeedPlus.View viewListener;

    public RetryViewHolder(View itemView, FeedPlus.View viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        button = itemView.findViewById(R.id.retry);
    }

    @Override
    public void bind(RetryModel element) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onRetryClicked();
            }
        });
    }
}