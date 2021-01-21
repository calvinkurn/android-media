
package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LikeDislike {

    @SerializedName("like_dislike_review")
    @Expose
    private List<LikeDislikeReview> likeDislikeReview = new ArrayList<LikeDislikeReview>();

    /**
     * 
     * @return
     *     The likeDislikeReview
     */
    public List<LikeDislikeReview> getLikeDislikeReview() {
        return likeDislikeReview;
    }

    /**
     * 
     * @param likeDislikeReview
     *     The like_dislike_review
     */
    public void setLikeDislikeReview(List<LikeDislikeReview> likeDislikeReview) {
        this.likeDislikeReview = likeDislikeReview;
    }

}
