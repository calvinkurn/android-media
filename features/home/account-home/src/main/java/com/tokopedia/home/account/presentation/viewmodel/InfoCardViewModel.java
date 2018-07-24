package com.tokopedia.home.account.presentation.viewmodel;

import android.support.annotation.DrawableRes;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;

/**
 * @author okasurya on 7/24/18.
 */
public class InfoCardViewModel implements Visitable<AccountTypeFactory> {
    @DrawableRes
    private int iconRes;
    private String iconUrl;
    private String mainText;
    private String secondaryText;
    private String applink;

    @Override
    public int type(AccountTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(@DrawableRes int iconRes) {
        this.iconRes = iconRes;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }
}
