package com.tokopedia.navigation.domain;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;

/**
 * Created by meta on 15/07/18.
 */
public class Inbox {

    private @DrawableRes int icon;
    private String title;
    private String subtitle;

    public Inbox(int icon, String title, String subtitle) {
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
