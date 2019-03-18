package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

import com.google.android.gms.tagmanager.DataLayer;
import java.util.Map;

public class SpotlightItemViewModel {
    private int id;
    private String title;
    private String description;
    private String backgroundImageUrl;
    private String tagName;
    private String tagNameHexcolor;
    private String tagHexcolor;
    private String ctaText;
    private String ctaTextHexcolor;
    private String url;
    private String applink;
    private String promoName;

    public SpotlightItemViewModel(int id, String title, String description,
                                  String backgroundImageUrl, String tagName,
                                  String tagNameHexcolor, String tagHexcolor,
                                  String ctaText, String ctaTextHexcolor,
                                  String url, String applink, String promoName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.backgroundImageUrl = backgroundImageUrl;
        this.tagName = tagName;
        this.tagNameHexcolor = tagNameHexcolor;
        this.tagHexcolor = tagHexcolor;
        this.ctaText = ctaText;
        this.ctaTextHexcolor = ctaTextHexcolor;
        this.url = url;
        this.applink = applink;
        this.promoName = promoName;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public String getTagName() {
        return tagName;
    }

    public String getTagNameHexcolor() {
        return tagNameHexcolor;
    }

    public String getTagHexcolor() {
        return tagHexcolor;
    }

    public String getCtaText() {
        return ctaText;
    }

    public String getCtaTextHexcolor() {
        return ctaTextHexcolor;
    }

    public String getUrl() {
        return url;
    }

    public String getApplink() {
        return applink;
    }

    public Map<String, Object> getEnhanceClickSpotlightHomePage(int position) {
        return DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", "homepage",
                "eventAction", "click on banner spotlight",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "promoClick", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", id,
                                                "name", promoName,
                                                "position", String.valueOf(position + 1),
                                                "creative", title,
                                                "creative_url", backgroundImageUrl
                                        )
                                )
                        )
                ),
                "attribution", ""
        );
    }
}
