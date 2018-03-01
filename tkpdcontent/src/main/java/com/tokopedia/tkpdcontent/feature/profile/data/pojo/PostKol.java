package com.tokopedia.tkpdcontent.feature.profile.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class PostKol {
  @SerializedName("isLiked")
  @Expose
  public Boolean isLiked;

  @SerializedName("isFollowed")
  @Expose
  public Boolean isFollowed;

  @SerializedName("id")
  @Expose
  public Integer id;

  @SerializedName("commentCount")
  @Expose
  public Integer commentCount;

  @SerializedName("likeCount")
  @Expose
  public Integer likeCount;

  @SerializedName("createTime")
  @Expose
  public String createTime;

  @SerializedName("description")
  @Expose
  public String description;

  @SerializedName("content")
  @Expose
  public List<PostKolContent> content;

  @SerializedName("userName")
  @Expose
  public String userName;

  @SerializedName("userInfo")
  @Expose
  public String userInfo;

  @SerializedName("userIsFollow")
  @Expose
  public Boolean userIsFollow;

  @SerializedName("userPhoto")
  @Expose
  public String userPhoto;

  @SerializedName("userUrl")
  @Expose
  public String userUrl;

  @SerializedName("userId")
  @Expose
  public Integer userId;
}
