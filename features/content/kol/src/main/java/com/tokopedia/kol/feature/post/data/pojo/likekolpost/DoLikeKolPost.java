
package com.tokopedia.kol.feature.post.data.pojo.likekolpost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.kol.feature.post.data.pojo.LikeKolPostSuccessData;

public class DoLikeKolPost {

    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("data")
    @Expose
    public LikeKolPostSuccessData data;

}
