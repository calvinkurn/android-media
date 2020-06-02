package com.tokopedia.buyerorder.list.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DotMenuList {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("uri")
    @Expose
    private String uri;

    public DotMenuList(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }

    public String name() {
        return name;
    }

    public String uri() {
        return uri;
    }
}
