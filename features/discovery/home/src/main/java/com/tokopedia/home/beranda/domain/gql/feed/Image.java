package com.tokopedia.home.beranda.domain.gql.feed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("s_url")
    @Expose
    private String s_url;

    @SerializedName("s_ecs")
    @Expose
    private String s_ecs;

    public String getS_url() {
        return s_url;
    }

    public String getS_ecs() {
        return s_ecs;
    }
}
