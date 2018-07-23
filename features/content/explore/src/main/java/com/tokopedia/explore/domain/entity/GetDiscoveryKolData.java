
package com.tokopedia.explore.domain.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDiscoveryKolData {

    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("categories")
    @Expose
    public List<Category> categories = null;
    @SerializedName("postKol")
    @Expose
    public List<PostKol> postKol = null;
    @SerializedName("lastCursor")
    @Expose
    public String lastCursor;

}
