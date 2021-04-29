package com.tokopedia.kol.feature.comment.view.adapter.viewholder;

import android.view.View;

import androidx.annotation.LayoutRes;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.feedcomponent.view.viewmodel.mention.MentionableUserViewModel;
import com.tokopedia.kol.R;
import com.tokopedia.kol.feature.comment.view.custom.KolCommentNewCardView;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentHeaderNewModel;
import com.tokopedia.unifycomponents.ProgressBarUnify;
import com.tokopedia.unifyprinciples.Typography;

import org.jetbrains.annotations.NotNull;

public class KolCommentHeaderNewViewHolder extends AbstractViewHolder<KolCommentHeaderNewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.kol_new_comment_header;

    private final KolComment.View viewListener;

    private KolCommentNewCardView commentView;
    private Typography loadMore;
    private ProgressBarUnify progressBar;

    private KolCommentNewCardView.Listener commentViewListener = new KolCommentNewCardView.Listener() {


        @Override
        public void onReport(@NotNull String reasonType, @NotNull String reasonDesc, @NotNull String id, boolean canDeleteComment) {

        }

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

    public KolCommentHeaderNewViewHolder(View itemView, KolComment.View viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        commentView = itemView.findViewById(R.id.kcv_comment);
        loadMore = itemView.findViewById(R.id.btn_load_more);
        progressBar = itemView.findViewById(R.id.progress_bar);
    }

    @Override
    public void bind(final KolCommentHeaderNewModel element) {
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
