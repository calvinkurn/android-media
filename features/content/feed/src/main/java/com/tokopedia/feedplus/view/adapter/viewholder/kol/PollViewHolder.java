package com.tokopedia.feedplus.view.adapter.viewholder.kol;

import android.support.annotation.LayoutRes;
import android.view.View;

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
    private BaseKolView baseKolView;

    @LayoutRes
    public static final int LAYOUT = R.layout.poll_layout;

    public PollViewHolder(View itemView, FeedPlus.View.Kol viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        baseKolView = itemView.findViewById(com.tokopedia.kol.R.id.base_kol_view);
        View view = baseKolView.inflateContentLayout(R.layout.poll_content);
    }

    @Override
    public void bind(PollViewModel element) {

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
