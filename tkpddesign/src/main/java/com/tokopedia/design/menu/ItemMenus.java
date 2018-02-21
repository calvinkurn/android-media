package com.tokopedia.design.menu;

/**
 * Created by meyta on 2/21/18.
 */

public class ItemMenus {

    public String title;
    public int icon;

    public ItemMenus(String title) {
        this.title = title;
    }

    public ItemMenus(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }
}