package com.tokopedia.shop.common.graphql.data.shopnote;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry on 08/08/18.
 */

public class ShopNoteModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("isTerms")
    @Expose
    private Boolean isTerms;
    @SerializedName("updateTime")
    @Expose
    private String updateTime;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Boolean getTerms() {
        return isTerms;
    }

    public String getUpdateTime() {
        return updateTime;
    }
}
