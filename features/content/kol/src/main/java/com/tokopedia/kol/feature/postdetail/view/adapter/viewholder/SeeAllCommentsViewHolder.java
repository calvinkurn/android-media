package com.tokopedia.kol.feature.postdetail.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.kol.R;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;
import com.tokopedia.kol.feature.postdetail.view.viewmodel.SeeAllCommentsViewModel;

/**
 * @author by milhamj on 27/07/18.
 */

public class SeeAllCommentsViewHolder extends AbstractViewHolder<SeeAllCommentsViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.kol_comment_see_all;

    private Context context;
    private KolComment.View.SeeAll listener;
    private TextView seeAllComment;

    public SeeAllCommentsViewHolder(View itemView, KolComment.View.SeeAll listener) {
        super(itemView);
        context = itemView.getContext();
        seeAllComment = itemView.findViewById(R.id.see_all_comment);

        this.listener = listener;
    }

    @Override
    public void bind(SeeAllCommentsViewModel element) {
        if (element.isShowSeeMore()) {
            seeAllComment.setVisibility(View.VISIBLE);
        } else {
            seeAllComment.setVisibility(View.GONE);
        }

        seeAllComment.setText(
                String.format(
                        context.getString(R.string.see_all_comment),
                        element.getTotalComments()
                )
        );
        seeAllComment.setOnClickListener(v ->
                listener.onGoToKolComment(getAdapterPosition(), element.getPostId())
        );
    }
}
