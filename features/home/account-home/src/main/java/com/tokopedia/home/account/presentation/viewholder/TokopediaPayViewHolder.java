package com.tokopedia.home.account.presentation.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayViewModel;

/**
 * @author okasurya on 7/19/18.
 */
public class TokopediaPayViewHolder extends AbstractViewHolder<TokopediaPayViewModel> {
    @LayoutRes
    public static int LAYOUT = R.layout.item_tokopedia_pay;

    public TokopediaPayViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(TokopediaPayViewModel element) {

    }
}
