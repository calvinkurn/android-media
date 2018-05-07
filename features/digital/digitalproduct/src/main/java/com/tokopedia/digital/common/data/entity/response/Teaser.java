package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 4/28/17.
 */

public class Teaser {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("content")
    @Expose
    private String content;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
