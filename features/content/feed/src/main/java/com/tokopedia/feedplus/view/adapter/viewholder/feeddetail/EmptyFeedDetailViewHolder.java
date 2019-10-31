package com.tokopedia.feedplus.view.adapter.viewholder.feeddetail;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.listener.FeedPlusDetail;

/**
 * @author by nisie on 6/22/17.
 */

public class EmptyFeedDetailViewHolder extends AbstractViewHolder<EmptyModel> {

    TextView buttonReturn;

    @LayoutRes
    public static final int LAYOUT = R.layout.list_feed_detail_empty;

    public EmptyFeedDetailViewHolder(View itemView, final FeedPlusDetail.View viewListener) {
        super(itemView);
        buttonReturn = (TextView) itemView.findViewById(R.id.button_return);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onBackPressed();
            }
        });
    }

    @Override
    public void bind(EmptyModel element) {

    }
}
