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
    private final Context context;

    private TextView comment;
    private TextView time;
    private ImageView avatar;
    private ImageView badge;
    private TextView loadMore;
    private ProgressBar progressBar;

    public KolCommentHeaderViewHolder(View itemView, KolComment.View viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        this.context = itemView.getContext();
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

        avatar.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(element.getUrl())) {
                viewListener.openRedirectUrl(element.getUrl());
            } else {
                viewListener.openRedirectUrl(constructProfileApplink(element.getUserId()));
            }
        });

        badge.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(element.getUserBadges())) {
            badge.setVisibility(View.VISIBLE);
            ImageHandler.loadImageCircle2(badge.getContext(), badge, element.getUserBadges());
        }

        String caption;
        if (badge.getVisibility() == View.VISIBLE) {
            caption = SPACE + getCommentText(element);
        } else {
            caption = getCommentText(element);
        }

        if (!TextUtils.isEmpty(element.getTagsLink())) {
            UrlUtil.setTextWithClickableTokopediaUrl(comment,
                    caption,
                    getUrlClickableSpan(element));
        } else {
            UrlUtil.setTextWithClickableTokopediaUrl(comment, caption);
        }

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

    private String getCommentText(KolCommentViewModel element) {
        return "<b>" + element.getName() + "</b>" + " "
                + element.getReview().toString().replaceAll("(\r\n|\n)", "<br />");
    }

    private String constructProfileApplink(String userId) {
        return ApplinkConst.PROFILE.replace(ApplinkConst.Profile.PARAM_USER_ID, userId);
    }

    private ClickableSpan getUrlClickableSpan(KolCommentHeaderViewModel element) {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                viewListener.openRedirectUrl(element.getTagsLink());
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(MethodChecker.getColor(context, R.color.tkpd_main_green));
            }
        };
    }
}
