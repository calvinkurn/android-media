package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel;

import android.support.annotation.IntegerRes;

/**
 * @author by errysuprayogi on 11/28/17.
 */
public class LayoutSections {
    String title;
    String icon;
    String applink;

    public LayoutSections(String title, String icon, String applink) {
        this.title = title;
        this.icon = icon;
        this.applink = applink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }
}
