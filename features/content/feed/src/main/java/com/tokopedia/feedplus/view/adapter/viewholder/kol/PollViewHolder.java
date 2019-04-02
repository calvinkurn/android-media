package com.tokopedia.feedplus.view.adapter.viewholder.kol;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.viewmodel.kol.PollViewModel;
import com.tokopedia.kol.feature.post.view.listener.BaseKolListener;
import com.tokopedia.kol.feature.post.view.viewmodel.BaseKolViewModel;
import com.tokopedia.kol.feature.post.view.widget.BaseKolView;

/**
 * @author by milhamj on 14/05/18.
 */

public class PollViewHolder extends AbstractViewHolder<PollViewModel> implements BaseKolListener {

    private static final int SPAN_COUNT = 2;
    private FeedPlus.View.Kol viewListener;
    private FeedPlus.View.Polling pollingViewListener;
    private BaseKolView baseKolView;
    private RecyclerView pollList;
    private TextView totalVoter;

    @LayoutRes
    public static final int LAYOUT = R.layout.poll_layout;

    public PollViewHolder(View itemView, FeedPlus.View.Kol viewListener,
                          FeedPlus.View.Polling pollingViewListener) {
        super(itemView);

        this.viewListener = viewListener;
        this.pollingViewListener = pollingViewListener;
        baseKolView = itemView.findViewById(com.tokopedia.kol.R.id.base_kol_view);
        View view = baseKolView.inflateContentLayout(R.layout.poll_content);
        pollList = view.findViewById(R.id.poll_list);
        totalVoter = view.findViewById(R.id.total_voter);
    }

    @Override
    public void bind(PollViewModel element) {
        baseKolView.bind(element);

        String totalVoterText = String.format(
                getString(R.string.total_voter),
                element.getTotalVoter()
        );

        totalVoter.setText(totalVoterText);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                itemView.getContext(),
                SPAN_COUNT,
                GridLayoutManager.VERTICAL,
                false);
        pollList.setLayoutManager(gridLayoutManager);

        PollAdapter pollAdapter = new PollAdapter(getAdapterPosition(),
                element,
                pollingViewListener);
        pollList.setAdapter(pollAdapter);
        pollAdapter.setList(element.getOptionViewModels());

        baseKolView.setViewListener(this, element);
    }

    @Override
    public void onAvatarClickListener(BaseKolViewModel element) {
        goToProfile(element);
    }

    @Override
    public void onNameClickListener(BaseKolViewModel element) {
        goToProfile(element);
    }

    @Override
    public void onFollowButtonClickListener(BaseKolViewModel element) {

    }

    @Override
    public void onDescriptionClickListener(BaseKolViewModel element) {

    }

    @Override
    public void onLikeButtonClickListener(BaseKolViewModel element) {
        if (element.isLiked()) {
            viewListener.onUnlikeKolClicked(getAdapterPosition(), element.getContentId());
        } else {
            viewListener.onLikeKolClicked(getAdapterPosition(), element.getContentId());
        }
    }

    @Override
    public void onCommentClickListener(BaseKolViewModel element) {
        viewListener.onGoToKolComment(getAdapterPosition(), element.getContentId());
    }

    @Override
    public void onMenuClickListener(BaseKolViewModel element) {

    }

    private void goToProfile(final BaseKolViewModel element) {
        viewListener.onGoToLink(element.getKolProfileUrl());
    }
}
