
package com.tokopedia.kol.feature.post.data.pojo.shop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Interaction {

    @SerializedName("isLiked")
    @Expose
    public boolean isLiked;
    @SerializedName("commentCount")
    @Expose
    public int commentCount;
    @SerializedName("likeCount")
    @Expose
    public int likeCount;
    @SerializedName("showComment")
    @Expose
    public boolean showComment;
    @SerializedName("showLike")
    @Expose
    public boolean showLike;

}
