
package com.tokopedia.kol.feature.comment.data.pojo.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Content {

    @SerializedName("imageurl")
    @Expose
    private String imageurl = "";
    @SerializedName("tags")
    @Expose
    private List<Tag> tags = new ArrayList<>();

    public Content(String imageurl, List<Tag> tags) {
        this.imageurl = imageurl;
        this.tags = tags;
    }

    public String getImageurl() {
        return imageurl;
    }

    public List<Tag> getTags() {
        return tags;
    }
}
