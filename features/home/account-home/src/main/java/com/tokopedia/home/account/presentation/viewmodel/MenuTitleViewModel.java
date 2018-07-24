package com.tokopedia.home.account.presentation.viewmodel;

import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.viewholder.MenuTitleViewHolder;

/**
 * @author okasurya on 7/23/18.
 */
public class MenuTitleViewModel implements Visitable<AccountTypeFactory> {
    private String title;

    @Override
    public int type(AccountTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
