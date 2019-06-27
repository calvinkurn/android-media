package com.tokopedia.navigation.domain.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.navigation.presentation.adapter.InboxTypeFactory;

/**
 * Created by meta on 15/07/18.
 */
public class Inbox implements Visitable<InboxTypeFactory> {

    private @DrawableRes Integer icon;
    private @StringRes Integer title;
    private @StringRes Integer subtitle;
    private String totalBadge;

    public Inbox(Integer icon, Integer title, Integer subtitle) {
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public Integer getTitle() {
        return title;
    }

    public void setTitle(Integer title) {
        this.title = title;
    }

    public Integer getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(Integer subtitle) {
        this.subtitle = subtitle;
    }

    public String getTotalBadge() {
        return totalBadge;
    }

    public void setTotalBadge(String totalBadge) {
        this.totalBadge = totalBadge;
    }

    @Override
    public int type(InboxTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
