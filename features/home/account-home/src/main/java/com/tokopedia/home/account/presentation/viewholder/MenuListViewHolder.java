package com.tokopedia.home.account.presentation.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.view.ItemListView;
import com.tokopedia.home.account.presentation.viewmodel.MenuListViewModel;
import com.tokopedia.unifycomponents.Label;

/**
 * @author okasurya on 7/23/18.
 */
public class MenuListViewHolder extends AbstractViewHolder<MenuListViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_label_view;

    private long lastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 1000;

    private View layout;
    private ItemListView labelView;
    private AccountItemListener listener;
    private View separator;
    private Label betaView;

    public MenuListViewHolder(View itemView, AccountItemListener listener) {
        super(itemView);
        layout = itemView.findViewById(R.id.container);
        labelView = itemView.findViewById(R.id.labelview);
        separator = itemView.findViewById(R.id.separator);
        betaView = itemView.findViewById(R.id.beta_label_view);
        this.listener = listener;
    }

    @Override
    public void bind(MenuListViewModel element) {
        layout.setOnClickListener(v -> {
            long now = System.currentTimeMillis();
            if (now - lastClickTime >= CLICK_TIME_INTERVAL) {
                if (element.getApplink().equalsIgnoreCase(AccountConstants.Navigation.TOPADS)){
                    listener.onTopAdsMenuClicked();
                } else {
                    listener.onMenuListClicked(element);
                }
                lastClickTime = now;
            }
        });
        labelView.setTitle(element.getMenu());
        labelView.setBadgeCounter(element.getCount());
        labelView.setSubTitle(element.getMenuDescription());
        labelView.setBetaLabel(element.isBeta());

        if (element.isUseSeparator()) {
            separator.setVisibility(View.VISIBLE);
        } else {
            separator.setVisibility(View.GONE);
        }
    }
}
