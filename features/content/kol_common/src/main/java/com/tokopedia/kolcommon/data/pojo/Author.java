package com.tokopedia.kolcommon.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 27/03/19.
 */
public class Author {

    public static String TYPE_AFFILIATE = "affiliate";
    public static String TYPE_SHOP = "content-shop";

    @SerializedName("id")
    @Expose
    private String id = "";

    @SerializedName("title")
    @Expose
    private String title = "";

    @SerializedName("name")
    @Expose
    private String name = "";

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail = "";

    @SerializedName("link")
    @Expose
    private String link = "";

    @SerializedName("badge")
    @Expose
    private String badge = "";

    @SerializedName("type")
    @Expose
    private String type = "";

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getBadge() {
        return badge;
    }

    public String getType() {
        return type;
    }
}
