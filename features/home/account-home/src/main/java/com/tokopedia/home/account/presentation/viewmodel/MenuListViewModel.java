package com.tokopedia.home.account.presentation.viewmodel;

import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.viewholder.MenuListViewHolder;

/**
 * @author okasurya on 7/23/18.
 */
public class MenuListViewModel implements Visitable<AccountTypeFactory> {
    private String menu;
    private String menuDescription;
    private String applink;

    @Override
    public int type(AccountTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getMenuDescription() {
        return menuDescription;
    }

    public void setMenuDescription(String menuDescription) {
        this.menuDescription = menuDescription;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }
}
