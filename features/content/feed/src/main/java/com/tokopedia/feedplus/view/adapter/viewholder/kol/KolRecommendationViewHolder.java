package com.tokopedia.feedplus.view.adapter.viewholder.kol;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.analytics.FeedAnalytics;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.viewmodel.kol.KolRecommendationViewModel;

/**
 * @author by nisie on 10/27/17.
 */

public class KolRecommendationViewHolder extends AbstractViewHolder<KolRecommendationViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.kol_recommend_layout;
    private final FeedPlus.View.Kol kolViewListener;
    private final FeedAnalytics analytics;

    private TextView title;
    private RecyclerView listRecommendation;
    private KolRecommendationAdapter adapter;
    private TextView seeAll;

    public KolRecommendationViewHolder(View itemView, FeedPlus.View.Kol kolViewListener, FeedAnalytics analytics) {
        super(itemView);
        this.kolViewListener = kolViewListener;
        this.analytics = analytics;
        title = itemView.findViewById(R.id.title);
        seeAll = itemView.findViewById(R.id.see_all_text);
        listRecommendation = itemView.findViewById(R.id.list_recommendation);
        adapter = new KolRecommendationAdapter(kolViewListener, analytics);
        listRecommendation.setAdapter(adapter);

    }

    @Override
    public void bind(final KolRecommendationViewModel element) {
        element.setRowNumber(getAdapterPosition());
        title.setText(MethodChecker.fromHtml(element.getTitle()));
        seeAll.setText(MethodChecker.fromHtml(element.getExploreText()));
        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analytics.eventKolRecommendationViewAllClick();
                kolViewListener.onGoToListKolRecommendation(element.getPage(), element
                        .getRowNumber(), element.getUrl());
            }
        });

        adapter = new KolRecommendationAdapter(kolViewListener, analytics);
        listRecommendation.setAdapter(adapter);
        adapter.setData(element);

    }
}
