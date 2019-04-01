package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

import com.google.android.gms.tagmanager.DataLayer;

import java.util.Map;

/**
 * @author by errysuprayogi on 11/28/17.
 */
public class HomeIconItem {
    private String id;
    String title;
    String icon;
    String applink;
    String url;

    public HomeIconItem(String id, String title, String icon, String applink, String url) {
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.applink = applink;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getEnhanceClickDynamicIconHomePage(int position) {
        return DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", "homepage",
                "eventAction", "click on dynamic icon",
                "eventLabel", title,
                "ecommerce", DataLayer.mapOf(
                        "promoClick", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", id,
                                                "name", "/ - dynamic icon",
                                                "position", String.valueOf(position + 1),
                                                "creative", title,
                                                "creative_url", icon
                                        )
                                )
                        )
                )
        );
    }
}
