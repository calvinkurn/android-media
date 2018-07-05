package com.tokopedia.contactus.home.data;

import com.google.gson.annotations.SerializedName;

public class ContactUsArticleResponse {

    @SerializedName("guid")
    private RenderedData guid;

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("slug")
    private String slug;

    @SerializedName("content")
    private String content;

    public void setGuid(RenderedData guid) {
        this.guid = guid;
    }

    public RenderedData getGuid() {
        return guid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return
                "ContactUsArticleResponse{" +
                        "guid = '" + guid + '\'' +
                        ",id = '" + id + '\'' +
                        ",title = '" + title + '\'' +
                        "}";
    }
}