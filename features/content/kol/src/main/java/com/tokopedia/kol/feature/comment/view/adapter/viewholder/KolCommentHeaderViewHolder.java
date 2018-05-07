package com.tokopedia.kol.feature.comment.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.kol.R;
import com.tokopedia.kol.common.util.UrlUtil;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentHeaderViewModel;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentViewModel;

/**
 * @author by nisie on 11/2/17.
 */

public class KolCommentHeaderViewHolder extends AbstractViewHolder<KolCommentHeaderViewModel> {


    @LayoutRes
    public static final int LAYOUT = R.layout.kol_comment_header;

    private static final String SPACE = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    private final KolComment.View viewListener;

    private TextView comment;
    private TextView time;
    private ImageView avatar;
    private ImageView badge;
    private TextView loadMore;
    private ProgressBar progressBar;

    public KolCommentHeaderViewHolder(View itemView, KolComment.View viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        avatar = itemView.findViewById(R.id.avatar);
        time = itemView.findViewById(R.id.time);
        comment = itemView.findViewById(R.id.comment);
        badge = itemView.findViewById(R.id.badge);
        loadMore = itemView.findViewById(R.id.btn_load_more);
        progressBar = itemView.findViewById(R.id.progress_bar);
    }

    @Override
    public void bind(final KolCommentHeaderViewModel element) {
        ImageHandler.loadImageCircle2(avatar.getContext(), avatar, element.getAvatarUrl());
        time.setText(element.getTime());

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(element.getUrl()))
                    viewListener.onGoToProfile(element.getUrl());
            }
        });

        badge.setVisibility(View.VISIBLE);
        UrlUtil.setTextWithClickableTokopediaUrl(comment, SPACE + getCommentText(element));

        if (element.isCanLoadMore())
            loadMore.setVisibility(View.VISIBLE);
        else
            loadMore.setVisibility(View.GONE);

        if (element.isLoading())
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);

        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                element.setCanLoadMore(false);
                element.setLoading(true);
                progressBar.setVisibility(View.VISIBLE);
                loadMore.setVisibility(View.GONE);
                viewListener.loadMoreComments();
            }
        });

    }

    private String getCommentText(KolCommentViewModel element) {
        return "<b>" + element.getName() + "</b>" + " "
                + element.getReview().replaceAll("(\r\n|\n)", "<br />");
    }
}
