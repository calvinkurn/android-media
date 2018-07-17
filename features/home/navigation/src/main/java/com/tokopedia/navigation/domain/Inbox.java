package com.tokopedia.navigation.domain;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;

/**
 * Created by meta on 15/07/18.
 */
public class Inbox {

    private @DrawableRes Integer icon;
    private @StringRes Integer title;
    private @StringRes Integer subtitle;

    public Inbox(Integer icon, Integer title, Integer subtitle) {
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
    }

    public Integer getIcon() {
        return icon;
    }

    public Integer getTitle() {
        return title;
    }

    public Integer getSubtitle() {
        return subtitle;
    }
}
