package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class TickerMetadata {
    @Expose
    @SerializedName("text")
    private Map<String, String> text;

    @Expose
    @SerializedName("image")
    private Map<String, String> image;

    @Expose
    @SerializedName("link")
    private Map<String, String> link;

    public Map<String, String> getText() {
        return text;
    }

    public void setText(Map<String, String> text) {
        this.text = text;
    }

    public Map<String, String> getImage() {
        return image;
    }

    public void setImage(Map<String, String> image) {
        this.image = image;
    }

    public Map<String, String> getLink() {
        return link;
    }

    public void setLink(Map<String, String> link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "TickerMetadata{" +
                "text=" + text +
                ", image=" + image +
                ", link=" + link +
                '}';
    }
}
