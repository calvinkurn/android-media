package com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class DataResponseReviewStarCount {
    @SerializedName("rating_score")
    @Expose
    private String ratingScore;
    @SerializedName("total_review")
    @Expose
    private int totalReview;
    @SerializedName("detail")
    @Expose
    private List<DetailReviewStarCount> detail = null;

    public String getRatingScore() {
        return ratingScore;
    }

    public void setRatingScore(String ratingScore) {
        this.ratingScore = ratingScore;
    }

    public int getTotalReview() {
        return totalReview;
    }

    public void setTotalReview(int totalReview) {
        this.totalReview = totalReview;
    }

    public List<DetailReviewStarCount> getDetail() {
        return detail;
    }

    public void setDetail(List<DetailReviewStarCount> detail) {
        this.detail = detail;
    }
}
