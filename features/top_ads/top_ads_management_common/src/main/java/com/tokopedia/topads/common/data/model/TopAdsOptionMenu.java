package com.tokopedia.topads.common.data.model;

import android.support.annotation.DrawableRes;

/**
 * Created by hadi.putra on 24/05/18.
 */

public class TopAdsOptionMenu {
    private @DrawableRes int icon;
    private String title;
    private int id;

    public TopAdsOptionMenu(String title, int id) {
        this.title = title;
        this.id = id;
    }

    public TopAdsOptionMenu(int icon, String title, int id) {
        this.icon = icon;
        this.title = title;
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
