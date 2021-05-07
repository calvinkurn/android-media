package com.tokopedia.kol.feature.comment.view.adapter.viewholder;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.feedcomponent.view.viewmodel.mention.MentionableUserViewModel;
import com.tokopedia.kol.R;
import com.tokopedia.kol.feature.comment.view.custom.KolCommentNewCardView;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentNewModel;

import org.jetbrains.annotations.NotNull;

public class KolCommentNewViewHolder extends AbstractViewHolder<KolCommentNewModel> {

    public static final int LAYOUT = R.layout.kol_comment_new_item;


    private final KolComment.View.ViewHolder viewListener;
    private final boolean canComment;

    private KolCommentNewCardView commentView;

    private KolCommentNewCardView.Listener commentViewListener = new KolCommentNewCardView.Listener() {
        @Override
        public void onReport(@NotNull String reasonType, @NotNull String reasonDesc, @NotNull String id, boolean canDeleteComment) {
            viewListener.reportAction(getAdapterPosition(), canDeleteComment, id,reasonType,reasonDesc);
        }

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

        @Override
        public void onReplyClicked(@NotNull MentionableUserViewModel mentionableUser) {
            viewListener.replyToUser(mentionableUser);
        }
    };

    public KolCommentNewViewHolder(View itemView, final KolComment.View.ViewHolder viewListener, boolean canComment) {
        super(itemView);
        this.viewListener = viewListener;
        this.canComment = canComment;
        commentView = itemView.findViewById(R.id.kcv_comment);
    }

    @Override
    public void bind(final KolCommentNewModel element) {
        commentView.setListener(commentViewListener);
        commentView.setModel(element, canComment);
    }
}
