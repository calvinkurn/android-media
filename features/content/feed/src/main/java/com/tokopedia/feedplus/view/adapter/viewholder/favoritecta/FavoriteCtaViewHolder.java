package com.tokopedia.feedplus.view.adapter.viewholder.favoritecta;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.viewmodel.FavoriteCtaViewModel;

/**
 * Created by henrypriyono on 11/30/17.
 */

public class FavoriteCtaViewHolder extends AbstractViewHolder<FavoriteCtaViewModel> {
    private TextView searchShopButton;
    private TextView titleTextView;
    private TextView subtitleTextView;

    @LayoutRes
    public static final int LAYOUT = R.layout.list_feed_favorite_cta;

    public FavoriteCtaViewHolder(View itemView, final FeedPlus.View viewListener) {
        super(itemView);

        titleTextView = itemView.findViewById(R.id.title);
        subtitleTextView = itemView.findViewById(R.id.subtitle);
        searchShopButton = itemView.findViewById(R.id.search_shop_button);

        searchShopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onSearchShopButtonClicked();
            }
        });
    }

    @Override
    public void bind(FavoriteCtaViewModel model) {
        titleTextView.setText(model.getTitle());
        subtitleTextView.setText(model.getSubtitle());
    }
}
