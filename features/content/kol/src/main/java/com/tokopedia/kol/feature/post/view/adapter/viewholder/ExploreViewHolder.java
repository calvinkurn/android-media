package com.tokopedia.kol.feature.post.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.ButtonCompat;
import com.tokopedia.kol.R;
import com.tokopedia.kol.feature.post.view.viewmodel.ExploreViewModel;

/**
 * @author by milhamj on 18/05/18.
 */

public class ExploreViewHolder extends AbstractViewHolder<ExploreViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.explore_layout;

    private static final String EXPLORE_URL = "tokopedia://content/explore/inspirasi/0";

    private ButtonCompat exploreMoreButton;

    public ExploreViewHolder(View itemView) {
        super(itemView);
        exploreMoreButton = itemView.findViewById(R.id.explore_more_button);
    }

    @Override
    public void bind(ExploreViewModel element) {
        exploreMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteManager.route(exploreMoreButton.getContext(), EXPLORE_URL);
            }
        });
    }
}
