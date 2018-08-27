
package com.tokopedia.kol.feature.post.data.pojo.shop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tag {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("link")
    @Expose
    public String link;
    @SerializedName("captionEng")
    @Expose
    public String captionEng;
    @SerializedName("captionInd")
    @Expose
    public String captionInd;

}
