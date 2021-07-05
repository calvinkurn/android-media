package com.tokopedia.tkpd.tkpdreputation.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 9/27/17.
 */

public class DeleteReviewResponsePojo {
    @SerializedName("is_success")
    @Expose
    private int isSuccess;

    public int getIsSuccess() {
        return isSuccess;
    }

}
