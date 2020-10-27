package com.tokopedia.shop.review.shop.data.pojo.likedislike

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 9/29/17.
 */
class GetLikeDislikePojo {
    @SerializedName("list")
    @Expose
    var list: List<LikeDislikeList> = listOf()

}