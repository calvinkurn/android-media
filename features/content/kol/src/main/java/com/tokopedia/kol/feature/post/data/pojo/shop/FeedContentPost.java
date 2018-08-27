
package com.tokopedia.kol.feature.post.data.pojo.shop;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedContentPost {

    @SerializedName("posts")
    @Expose
    public List<Post> posts = null;
    @SerializedName("lastCursor")
    @Expose
    public String lastCursor;
    @SerializedName("error")
    @Expose
    public String error;

}
