package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.BrandsViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.CategoryItemViewModel;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class CategoryItemViewHolder extends AbstractViewHolder<CategoryItemViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ticker;

    public CategoryItemViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(CategoryItemViewModel element) {

    }
}
