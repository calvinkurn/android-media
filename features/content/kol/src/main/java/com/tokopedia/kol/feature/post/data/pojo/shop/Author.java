
package com.tokopedia.kol.feature.post.data.pojo.shop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Author {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("thumbnail")
    @Expose
    public String thumbnail;
    @SerializedName("url")
    @Expose
    public String url;

}
