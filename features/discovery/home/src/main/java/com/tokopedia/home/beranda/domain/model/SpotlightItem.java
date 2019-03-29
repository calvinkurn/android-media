package com.tokopedia.home.beranda.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpotlightItem {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("title")
    @Expose
    private String title = "";
    @SerializedName("description")
    @Expose
    private String description = "";
    @SerializedName("background_image_url")
    @Expose
    private String backgroundImageUrl = "";
    @SerializedName("tag_name")
    @Expose
    private String tagName = "";
    @SerializedName("tag_name_hexcolor")
    @Expose
    private String tagNameHexcolor = "";
    @SerializedName("tag_hexcolor")
    @Expose
    private String tagHexcolor = "";
    @SerializedName("cta_text")
    @Expose
    private String ctaText = "";
    @SerializedName("cta_text_hexcolor")
    @Expose
    private String ctaTextHexcolor = "";
    @SerializedName("url")
    @Expose
    private String url = "";
    @SerializedName("applink")
    @Expose
    private String applink = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagNameHexcolor() {
        return tagNameHexcolor;
    }

    public void setTagNameHexcolor(String tagNameHexcolor) {
        this.tagNameHexcolor = tagNameHexcolor;
    }

    public String getTagHexcolor() {
        return tagHexcolor;
    }

    public void setTagHexcolor(String tagHexcolor) {
        this.tagHexcolor = tagHexcolor;
    }

    public String getCtaText() {
        return ctaText;
    }

    public void setCtaText(String ctaText) {
        this.ctaText = ctaText;
    }

    public String getCtaTextHexcolor() {
        return ctaTextHexcolor;
    }

    public void setCtaTextHexcolor(String ctaTextHexcolor) {
        this.ctaTextHexcolor = ctaTextHexcolor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }
}
