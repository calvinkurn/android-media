
package com.tokopedia.kolcommon.data.pojo.like;

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
