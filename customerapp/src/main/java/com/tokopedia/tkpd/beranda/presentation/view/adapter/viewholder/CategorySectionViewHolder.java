package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.CategorySectionViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.TickerViewModel;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class CategorySectionViewHolder extends AbstractViewHolder<CategorySectionViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ticker;

    public CategorySectionViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(CategorySectionViewModel element) {

    }
}
