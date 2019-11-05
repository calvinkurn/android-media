
package com.tokopedia.kol.feature.post.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.kol.feature.post.data.pojo.likekolpost.DoLikeKolPost;

public class LikeKolPostResponse {

    @SerializedName("do_like_kol_post")
    @Expose
    public DoLikeKolPost doLikeKolPost;

}
