package com.tokopedia.kol.feature.comment.view.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.kol.R;
import com.tokopedia.kol.feature.comment.view.custom.KolCommentCardView;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentViewModel;

import org.jetbrains.annotations.NotNull;

/**
 * @author by nisie on 10/31/17.
 */

public class KolCommentViewHolder extends AbstractViewHolder<KolCommentViewModel> {

    public static final int LAYOUT = R.layout.kol_comment_item;
    private final KolComment.View.ViewHolder viewListener;

    private KolCommentCardView commentView;

    private KolCommentCardView.Listener commentViewListener = new KolCommentCardView.Listener() {
        @Override
        public void onAvatarClicked(@NotNull String profileUrl) {
            viewListener.onGoToProfile(profileUrl);
        }

        @Override
        public void onMentionedProfileClicked(@NotNull String authorId) {
            viewListener.onClickMentionedProfile(authorId);
        }

        @Override
        public boolean onDeleteComment(@NotNull String commentId, boolean canDeleteComment) {
            return viewListener.onDeleteCommentKol(commentId, canDeleteComment, getAdapterPosition());
        }

        @Override
        public void onTokopediaUrlClicked(@NotNull String url) {

        }
    };

    public KolCommentViewHolder(View itemView, final KolComment.View.ViewHolder viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        commentView = itemView.findViewById(R.id.kcv_comment);
    }

    @Override
    public void bind(final KolCommentViewModel element) {
        commentView.setListener(commentViewListener);
        commentView.setModel(element);
    }
}
