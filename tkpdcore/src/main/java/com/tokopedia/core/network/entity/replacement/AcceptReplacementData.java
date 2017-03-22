package com.tokopedia.core.network.entity.replacement;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hangnadi on 3/3/17.
 */
public class AcceptReplacementData {
    @SerializedName("is_success")
    private int isSuccess;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return getIsSuccess() == 1;
    }
}
