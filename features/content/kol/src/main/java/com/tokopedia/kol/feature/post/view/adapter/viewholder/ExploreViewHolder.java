package com.tokopedia.kol.feature.post.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.ButtonCompat;
import com.tokopedia.kol.R;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.viewmodel.ExploreViewModel;

import static com.tokopedia.kol.analytics.KolEventTracking.Action.CLICK_PROMPT;
import static com.tokopedia.kol.analytics.KolEventTracking.Category.KOL_TOP_PROFILE;
import static com.tokopedia.kol.analytics.KolEventTracking.Event.EVENT_CLICK_TOP_PROFILE;
import static com.tokopedia.kol.analytics.KolEventTracking.EventLabel.GO_TO_EXPLORE_FORMAT;

/**
 * @author by milhamj on 18/05/18.
 */

public class ExploreViewHolder extends AbstractViewHolder<ExploreViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.explore_layout;

    private static final String EXPLORE_URL = "tokopedia://content/explore/inspirasi/0";

    private final KolPostListener.View.ViewHolder viewListener;
    private final ButtonCompat exploreMoreButton;

    public ExploreViewHolder(View itemView, KolPostListener.View.ViewHolder viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        exploreMoreButton = itemView.findViewById(R.id.explore_more_button);
    }

    @Override
    public void bind(final ExploreViewModel element) {
        exploreMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteManager.route(exploreMoreButton.getContext(), EXPLORE_URL);

                viewListener.getAbstractionRouter().getAnalyticTracker().sendEventTracking(
                        EVENT_CLICK_TOP_PROFILE,
                        KOL_TOP_PROFILE,
                        CLICK_PROMPT,
                        String.format(GO_TO_EXPLORE_FORMAT, element.getKolName())
                );
            }
        });
    }
}
