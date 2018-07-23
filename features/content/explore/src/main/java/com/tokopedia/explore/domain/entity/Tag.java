
package com.tokopedia.explore.domain.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tag {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("link")
    @Expose
    public String link;
    @SerializedName("price")
    @Expose
    public String price;
    @SerializedName("caption")
    @Expose
    public String caption;

}
