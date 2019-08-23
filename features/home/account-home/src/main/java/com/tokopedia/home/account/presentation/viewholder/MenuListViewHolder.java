package com.tokopedia.home.account.presentation.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.viewmodel.MenuListViewModel;

/**
 * @author okasurya on 7/23/18.
 */
public class MenuListViewHolder extends AbstractViewHolder<MenuListViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_label_view;

    private long lastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 1000;

    private View layout;
    private LabelView labelView;
    private AccountItemListener listener;

    public MenuListViewHolder(View itemView, AccountItemListener listener) {
        super(itemView);
        layout = itemView.findViewById(R.id.container);
        labelView = itemView.findViewById(R.id.labelview);

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
        labelView.showRightArrow(false);
    }
}
