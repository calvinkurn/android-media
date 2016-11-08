
package com.tokopedia.core.shoplocation.model.deletelocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteLocationResponse {

    @SerializedName("is_success")
    @Expose
    private Integer isSuccess;

    /**
     *
     * @return
     *     The isSuccess
     */
    public Integer getIsSuccess() {
        return isSuccess;
    }

    /**
     *
     * @param isSuccess
     *     The is_success
     */
    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }

}
