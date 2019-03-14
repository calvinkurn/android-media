package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

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

    public SpotlightItemViewModel(int id, String title, String description,
                                  String backgroundImageUrl, String tagName,
                                  String tagNameHexcolor, String tagHexcolor,
                                  String ctaText, String ctaTextHexcolor,
                                  String url, String applink) {
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
}
