package com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.sendreview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 9/5/17.
 */

public class SendReviewSubmitPojo {

    @SerializedName("review_id")
    @Expose
    private int reviewId;
    @SerializedName("is_success")
    @Expose
    private int isSuccess;

    public int getReviewId() {
        return reviewId;
    }

    public int getIsSuccess() {
        return isSuccess;
    }
}
