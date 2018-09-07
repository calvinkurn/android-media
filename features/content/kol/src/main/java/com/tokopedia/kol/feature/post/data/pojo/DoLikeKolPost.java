
package com.tokopedia.kol.feature.post.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoLikeKolPost {

    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("data")
    @Expose
    public LikeKolPostSuccessData data;

}
