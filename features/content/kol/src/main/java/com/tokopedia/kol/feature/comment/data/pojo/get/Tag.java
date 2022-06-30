
package com.tokopedia.kol.feature.comment.data.pojo.get;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tag {

    @SerializedName("id")
    @Expose
    private String id = "";
    @SerializedName("type")
    @Expose
    private String type = "";
    @SerializedName("link")
    @Expose
    private String link = "";
    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    @Expose
    private String price = "";
    @SerializedName("url")
    @Expose
    private String url = "";
    @SerializedName("caption")
    @Expose
    private String caption = "";

    public Tag(String id, String type, String link, String price, String url, String caption) {
        this.id = id;
        this.type = type;
        this.link = link;
        this.price = price;
        this.url = url;
        this.caption = caption;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getLink() {
        return link;
    }

    public String getPrice() {
        return price;
    }

    public String getUrl() {
        return url;
    }

    public String getCaption() {
        return caption;
    }
}
