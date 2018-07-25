package com.tokopedia.home.account.presentation.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.viewmodel.MenuListViewModel;

/**
 * @author okasurya on 7/23/18.
 */
public class MenuListViewHolder extends AbstractViewHolder<MenuListViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_common_subtitle;

    private LinearLayout layout;
    private ImageView icon;
    private TextView menu;
    private TextView menuDescription;
    private AccountTypeFactory.Listener listener;

    public MenuListViewHolder(View itemView, AccountTypeFactory.Listener listener) {
        super(itemView);
        layout = itemView.findViewById(R.id.layout_common_subtitle);
        icon = itemView.findViewById(R.id.icon);
        menu = itemView.findViewById(R.id.title);
        menuDescription = itemView.findViewById(R.id.subtitle);
        icon.setVisibility(View.GONE);

        this.listener = listener;
    }

    @Override
    public void bind(MenuListViewModel element) {
        layout.setOnClickListener(v -> listener.onMenuListClicked(element));
        menu.setText(element.getMenu());
        menuDescription.setText(element.getMenuDescription());
    }
}
