
package com.tokopedia.explore.domain.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Content {

    @SerializedName("imageurl")
    @Expose
    public String imageurl;
    @SerializedName("tags")
    @Expose
    public List<Tag> tags = null;

}
