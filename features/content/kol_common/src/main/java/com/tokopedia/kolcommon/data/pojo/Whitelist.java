package com.tokopedia.kolcommon.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author by yfsx on 20/06/18.
 */
public class Whitelist {

    @SerializedName("iswhitelist")
    private boolean isWhitelist;

    @SerializedName("url")
    private String url;

    @SerializedName("error")
    private String error;

    @SerializedName("title")
    private String title;

    @SerializedName("title_identifier")
    private String titleIdentifier;

    @SerializedName("description")
    private String description;

    @SerializedName("post_success")
    private String postSuccessMessage;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("authors")
    private ArrayList<Author> authors;

    public boolean isWhitelist() {
        return isWhitelist;
    }

    public String getUrl() {
        return url;
    }

    public String getError() {
        return error;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTitleIdentifier() {
        return titleIdentifier;
    }

    public String getPostSuccessMessage() {
        return postSuccessMessage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ArrayList<Author> getAuthors() {
        return authors;
    }
}
