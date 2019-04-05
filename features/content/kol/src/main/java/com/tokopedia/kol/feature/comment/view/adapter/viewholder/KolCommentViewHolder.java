package com.tokopedia.kol.feature.comment.view.adapter.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.kol.R;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentViewModel;

/**
 * @author by nisie on 10/31/17.
 */

public class KolCommentViewHolder extends AbstractViewHolder<KolCommentViewModel> {

    public static final int LAYOUT = R.layout.kol_comment_item;
    private static final String SPACE = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    private final KolComment.View.ViewHolder viewListener;

    private View mainView;
    private TextView comment;
    private TextView time;
    private ImageView avatar;
    private ImageView badge;

    public KolCommentViewHolder(View itemView, final KolComment.View.ViewHolder viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        avatar = itemView.findViewById(R.id.avatar);
        time = itemView.findViewById(R.id.time);
        comment = itemView.findViewById(R.id.comment);
        badge = itemView.findViewById(R.id.badge);
        mainView = itemView.findViewById(R.id.main_view);
    }

    @Override
    public void bind(final KolCommentViewModel element) {
        ImageHandler.loadImageCircle2(avatar.getContext(), avatar, element.getAvatarUrl());
        time.setText(element.getTime());

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(element.getUrl())) {
                    viewListener.onGoToProfile(element.getUrl());
                }
            }
        });

        if (element.isOfficial()) {
            badge.setVisibility(View.VISIBLE);
            comment.setText(MethodChecker.fromHtml(SPACE + getCommentText(element)));
        } else {
            badge.setVisibility(View.GONE);
            comment.setText(MethodChecker.fromHtml(getCommentText(element)));
        }

        mainView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return viewListener.onDeleteCommentKol(element.getId(),
                        element.canDeleteComment(), getAdapterPosition());

            }
        });

    }

    private String getCommentText(KolCommentViewModel element) {
        return "<b>" + element.getName() + "</b>" + " " + element.getReview();
    }
}
