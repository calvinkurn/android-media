package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.listener.HomeFeedsListener;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeRetryModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.RetryModel;

/**
 * Created by henrypriyono on 1/12/18.
 */

public class RetryViewHolder extends HomeAbstractViewHolder<HomeRetryModel> {
    @LayoutRes
    public final static int LAYOUT = R.layout.home_feeds_retry_layout;
    private final View button;
    private final HomeFeedsListener viewListener;

    public RetryViewHolder(View itemView, HomeFeedsListener viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        button = itemView.findViewById(R.id.retry);
    }

    @Override
    public void bind(HomeRetryModel element) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onRetryLoadFeeds();
            }
        });
    }
}
