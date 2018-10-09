package com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.sendreview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 9/4/17.
 */

public class SendReviewValidatePojo {

    @SerializedName("post_key")
    @Expose
    private String postKey;
    @SerializedName("review_id")
    @Expose
    private int reviewId;
    @SerializedName("is_success")
    @Expose
    private int isSuccess;

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

}
