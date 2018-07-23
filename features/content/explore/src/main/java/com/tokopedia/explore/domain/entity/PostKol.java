
package com.tokopedia.explore.domain.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostKol {

    @SerializedName("isLiked")
    @Expose
    public boolean isLiked;
    @SerializedName("isFollow")
    @Expose
    public boolean isFollow;
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("commentCount")
    @Expose
    public int commentCount;
    @SerializedName("likeCount")
    @Expose
    public int likeCount;
    @SerializedName("createTime")
    @Expose
    public String createTime;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("content")
    @Expose
    public List<Content> content = null;
    @SerializedName("userName")
    @Expose
    public String userName;
    @SerializedName("userInfo")
    @Expose
    public String userInfo;
    @SerializedName("userIsFollow")
    @Expose
    public boolean userIsFollow;
    @SerializedName("userPhoto")
    @Expose
    public String userPhoto;
    @SerializedName("userUrl")
    @Expose
    public String userUrl;
    @SerializedName("userId")
    @Expose
    public int userId;

}
