package com.tokopedia.kol.feature.comment.view.adapter.viewholder;

import android.view.View;

import androidx.annotation.LayoutRes;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.feedcomponent.view.viewmodel.mention.MentionableUserViewModel;
import com.tokopedia.kol.R;
import com.tokopedia.kol.feature.comment.view.custom.KolCommentCardView;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentHeaderViewModel;
import com.tokopedia.unifycomponents.ProgressBarUnify;
import com.tokopedia.unifyprinciples.Typography;

import org.jetbrains.annotations.NotNull;

/**
 * @author by nisie on 11/2/17.
 */

public class KolCommentHeaderViewHolder extends AbstractViewHolder<KolCommentHeaderViewModel> {


    @LayoutRes
    public static final int LAYOUT = R.layout.kol_comment_header;

    private final KolComment.View viewListener;

    private KolCommentCardView commentView;
    private Typography loadMore;
    private ProgressBarUnify progressBar;

    private KolCommentCardView.Listener commentViewListener = new KolCommentCardView.Listener() {
        @Override
        public void onAvatarClicked(@NotNull String profileUrl) {
            viewListener.openRedirectUrl(profileUrl);
        }

        @Override
        public void onMentionedProfileClicked(@NotNull String authorId) {
        }

        @Override
        public boolean onDeleteComment(@NotNull String commentId, boolean canDeleteComment) {
            return false;
        }

        @Override
        public void onTokopediaUrlClicked(@NotNull String url) {
            viewListener.openRedirectUrl(url);
        }

        @Override
        public void onReplyClicked(@NotNull MentionableUserViewModel mentionableUser) {
            viewListener.replyToUser(mentionableUser);
        }
    };

    public KolCommentHeaderViewHolder(View itemView, KolComment.View viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        commentView = itemView.findViewById(R.id.kcv_comment);
        loadMore = itemView.findViewById(R.id.btn_load_more);
        progressBar = itemView.findViewById(R.id.progress_bar);
    }

    @Override
    public void bind(final KolCommentHeaderViewModel element) {
        commentView.setListener(commentViewListener);
        commentView.setModel(element, true);

        if (element.isCanLoadMore())
            loadMore.setVisibility(View.VISIBLE);
        else
            loadMore.setVisibility(View.GONE);

        if (element.isLoading())
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);

        loadMore.setOnClickListener(v -> {
            element.setCanLoadMore(false);
            element.setLoading(true);
            progressBar.setVisibility(View.VISIBLE);
            loadMore.setVisibility(View.GONE);
            viewListener.loadMoreComments();
        });
    }
}
