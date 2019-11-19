package com.tokopedia.home.account.presentation.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.viewmodel.MenuTitleViewModel;

/**
 * @author okasurya on 7/23/18.
 */
public class MenuTitleViewHolder extends AbstractViewHolder<MenuTitleViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_menu_title;

    private TextView textCategoryTitle;

    public MenuTitleViewHolder(View itemView) {
        super(itemView);
        textCategoryTitle = itemView.findViewById(R.id.text_category_title);
    }

    @Override
    public void bind(MenuTitleViewModel element) {
        textCategoryTitle.setText(element.getTitle());
    }
}
