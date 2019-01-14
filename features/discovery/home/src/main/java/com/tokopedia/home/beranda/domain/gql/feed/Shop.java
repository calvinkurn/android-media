package com.tokopedia.home.beranda.domain.gql.feed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

public class Shop {
    @SerializedName("id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }
}
