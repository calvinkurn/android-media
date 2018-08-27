
package com.tokopedia.kol.feature.post.data.pojo.shop;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("createTime")
    @Expose
    public String createTime;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("content")
    @Expose
    public List<Content> content = null;
    @SerializedName("author")
    @Expose
    public Author author;
    @SerializedName("interaction")
    @Expose
    public Interaction interaction;

}
