package com.tokopedia.shop.review.shop.data.pojo.likedislike

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LikeDislikeList {
    @SerializedName("review_id")
    @Expose
    var reviewId = 0
    @SerializedName("total_like")
    @Expose
    var totalLike = 0
    @SerializedName("total_dislike")
    @Expose
    var totalDislike = 0
    @SerializedName("like_status")
    @Expose
    var likeStatus = 0

}