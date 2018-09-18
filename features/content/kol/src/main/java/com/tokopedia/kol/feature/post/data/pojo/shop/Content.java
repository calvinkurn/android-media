
package com.tokopedia.kol.feature.post.data.pojo.shop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Content {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("URL")
    @Expose
    private String uRL;
    @SerializedName("tags")
    @Expose
    private List<Tag> tags = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getuRL() {
        return uRL;
    }

    public void setuRL(String uRL) {
        this.uRL = uRL;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
