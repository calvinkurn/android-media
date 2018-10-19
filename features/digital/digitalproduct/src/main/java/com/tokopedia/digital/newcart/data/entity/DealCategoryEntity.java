package com.tokopedia.digital.newcart.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DealCategoryEntity {
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("title")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
