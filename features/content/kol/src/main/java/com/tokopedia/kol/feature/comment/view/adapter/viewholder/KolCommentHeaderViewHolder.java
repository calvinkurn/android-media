package com.tokopedia.kol.feature.comment.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.kol.R;
import com.tokopedia.kol.common.util.UrlUtil;
import com.tokopedia.kol.feature.comment.view.custom.KolCommentCardView;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentHeaderViewModel;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentViewModel;

import org.jetbrains.annotations.NotNull;

/**
 * @author by nisie on 11/2/17.
 */

public class KolCommentHeaderViewHolder extends AbstractViewHolder<KolCommentHeaderViewModel> {


    @LayoutRes
    public static final int LAYOUT = R.layout.kol_comment_header;

    private static final String SPACE = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    private final KolComment.View viewListener;
    private final Context context;

    private KolCommentCardView commentView;
    private TextView comment;
    private TextView loadMore;
    private ProgressBar progressBar;

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
    };

    public KolCommentHeaderViewHolder(View itemView, KolComment.View viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        this.context = itemView.getContext();
        commentView = itemView.findViewById(R.id.kcv_comment);
        comment = itemView.findViewById(R.id.comment);
        loadMore = itemView.findViewById(R.id.btn_load_more);
        progressBar = itemView.findViewById(R.id.progress_bar);
    }

    @Override
    public void bind(final KolCommentHeaderViewModel element) {
        commentView.setListener(commentViewListener);
        commentView.setModel(element);

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
