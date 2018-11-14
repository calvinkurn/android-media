package com.tokopedia.useridentification.view.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 12/11/18.
 */
public class GetApprovalStatusPojo {

    @Expose
    @SerializedName("is_success")
    private int isSuccess;

    public int getIsSuccess() {
        return isSuccess;
    }
}
