
package com.tokopedia.explore.domain.entity;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Content {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("imageurl")
    @Expose
    private String imageurl;
    @SerializedName("tags")
    @Expose
    private List<Tag> tags = null;

    public String getType() {
        return type;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

}
