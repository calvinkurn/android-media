package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder;


import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.listener.HomeCategoryListener;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.compoundview.HeaderHomeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 11/12/17.
 */

public class HeaderViewHolder extends AbstractViewHolder<HeaderViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_item_header_home;
    private final HomeCategoryListener listener;
    @BindView(R.id.container)
    LinearLayout mainContainer;

    public HeaderViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        this.listener = listener;
        ButterKnife.bind(this, itemView);

    }

    @Override
    public void bind(HeaderViewModel element) {
        if (element.getHomeHeaderWalletActionData() == null && element.getTokoPointDrawerData() == null)
            return;
        mainContainer.removeAllViews();
        if (element.getType() == HeaderViewModel.TYPE_EMPTY) return;
        mainContainer.addView(new HeaderHomeView(itemView.getContext(), element, listener), 0);
    }
}
