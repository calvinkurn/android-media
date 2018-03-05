package com.tokopedia.tkpdcontent.feature.profile.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdcontent.R;
import com.tokopedia.tkpdcontent.feature.profile.view.viewmodel.EmptyKolPostViewModel;

/**
 * @author by milhamj on 08/02/18.
 */

public class EmptyKolPostViewHolder extends AbstractViewHolder<EmptyKolPostViewModel> {

    @LayoutRes public static final int LAYOUT = R.layout.kol_post_empty;

    public EmptyKolPostViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(EmptyKolPostViewModel element) {

    }
}