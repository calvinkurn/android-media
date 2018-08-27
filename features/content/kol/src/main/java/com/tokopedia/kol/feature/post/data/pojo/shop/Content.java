
package com.tokopedia.kol.feature.post.data.pojo.shop;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Content {

    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("URL")
    @Expose
    public String uRL;
    @SerializedName("tags")
    @Expose
    public List<Tag> tags = null;

}
