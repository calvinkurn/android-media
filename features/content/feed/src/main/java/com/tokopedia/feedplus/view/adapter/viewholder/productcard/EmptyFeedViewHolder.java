package com.tokopedia.feedplus.view.adapter.viewholder.productcard;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.listener.FeedPlus;

/**
 * @author by nisie on 5/15/17.
 */

public class EmptyFeedViewHolder extends AbstractViewHolder<EmptyModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.list_feed_product_empty;

    public EmptyFeedViewHolder(View itemView, final FeedPlus.View viewListener) {
        super(itemView);
        Button searchShopButton = itemView.findViewById(R.id.search_shop_button);

        searchShopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onSearchShopButtonClicked();
            }
        });
    }

    @Override
    public void bind(EmptyModel emptyModel) {

    }


}
