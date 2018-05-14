package com.tokopedia.feedplus.view.adapter.viewholder.kol;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.viewmodel.kol.PollViewModel;

/**
 * @author by milhamj on 14/05/18.
 */

public class PollViewHolder extends AbstractViewHolder<PollViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.poll_layout;

    public PollViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(PollViewModel element) {

    }
}
