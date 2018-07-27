package com.tokopedia.kol.feature.postdetail.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.kol.R;
import com.tokopedia.kol.feature.postdetail.view.viewmodel.SeeAllCommentsViewModel;

/**
 * @author by milhamj on 27/07/18.
 */

public class SeeAllCommentsViewHolder extends AbstractViewHolder<SeeAllCommentsViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.explore_layout;

    public SeeAllCommentsViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(SeeAllCommentsViewModel element) {

    }
}
