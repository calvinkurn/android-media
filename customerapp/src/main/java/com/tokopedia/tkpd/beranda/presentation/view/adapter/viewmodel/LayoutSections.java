package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel;

import android.support.annotation.IntegerRes;

/**
 * @author by errysuprayogi on 11/28/17.
 */
public class LayoutSections {
    String title;
    @IntegerRes
    int icon;

    public LayoutSections(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
