package com.tokopedia.home.account.presentation.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridViewModel;

/**
 * @author okasurya on 7/19/18.
 */
public class MenuGridViewHolder extends AbstractViewHolder<MenuGridViewModel> {
    @LayoutRes
    public static int LAYOUT = R.layout.item_menu_grid;

    public MenuGridViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(MenuGridViewModel element) {

    }
}
