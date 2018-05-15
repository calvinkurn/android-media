package com.tokopedia.feedplus.view.adapter.viewholder.kol;

import android.support.annotation.LayoutRes;
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

    private FeedPlus.View.Kol viewListener;
    private PollAdapter pollAdapter;
    private BaseKolView baseKolView;
    private RecyclerView pollList;
    private TextView totalVoter;

    @LayoutRes
    public static final int LAYOUT = R.layout.poll_layout;

    public PollViewHolder(View itemView, FeedPlus.View.Kol viewListener) {
        super(itemView);
        pollAdapter = new PollAdapter();

        this.viewListener = viewListener;
        baseKolView = itemView.findViewById(com.tokopedia.kol.R.id.base_kol_view);
        View view = baseKolView.inflateContentLayout(R.layout.poll_content);
        pollList = view.findViewById(R.id.poll_list);
        totalVoter = view.findViewById(R.id.total_voter);
    }

    @Override
    public void bind(PollViewModel element) {
        String totalVoterText = String.format(
                getString(R.string.total_voter),
                element.getTotalVoter()
        );

        totalVoter.setText(totalVoterText);
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
            viewListener.onUnlikeKolClicked(getAdapterPosition(), element.getKolId());
        } else {
            viewListener.onLikeKolClicked(getAdapterPosition(), element.getKolId());
        }
    }

    @Override
    public void onCommentClickListener(BaseKolViewModel element) {
        viewListener.onGoToKolComment(getAdapterPosition(), element.getKolId());
    }

    private void goToProfile(final BaseKolViewModel element) {
        viewListener.onGoToKolProfile(getAdapterPosition(),
                String.valueOf(element.getUserId()),
                element.getContentId()
        );
    }
}
