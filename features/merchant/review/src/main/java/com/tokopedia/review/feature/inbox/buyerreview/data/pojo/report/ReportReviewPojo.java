package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 9/13/17.
 */

public class ReportReviewPojo {

    @SerializedName("is_success")
    @Expose
    private int isSuccess;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }
}
