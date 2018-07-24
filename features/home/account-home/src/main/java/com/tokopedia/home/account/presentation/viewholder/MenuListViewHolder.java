package com.tokopedia.home.account.presentation.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.account.presentation.viewmodel.MenuListViewModel;
import com.tokopedia.abstraction.R;
/**
 * @author okasurya on 7/23/18.
 */
public class MenuListViewHolder extends AbstractViewHolder<MenuListViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_common_subtitle;

    private ImageView icon;
    private TextView menu;
    private TextView menuDescription;

    public MenuListViewHolder(View itemView) {
        super(itemView);
        icon = itemView.findViewById(R.id.icon);
        menu = itemView.findViewById(R.id.title);
        menuDescription = itemView.findViewById(R.id.subtitle);
        icon.setVisibility(View.GONE);

    }

    @Override
    public void bind(MenuListViewModel element) {
        menu.setText(element.getMenu());
        menuDescription.setText(element.getMenuDescription());
    }
}
