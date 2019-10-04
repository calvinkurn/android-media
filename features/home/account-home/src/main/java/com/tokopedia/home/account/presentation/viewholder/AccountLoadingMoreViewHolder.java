package com.tokopedia.home.account.presentation.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.account.R;

public class AccountLoadingMoreViewHolder extends AbstractViewHolder<LoadingMoreModel> {
    @LayoutRes
    public final static int LAYOUT = R.layout.item_loading_more_account;

    public AccountLoadingMoreViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(LoadingMoreModel element) {

    }
}