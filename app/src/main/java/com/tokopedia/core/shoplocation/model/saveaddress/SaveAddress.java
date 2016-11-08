
package com.tokopedia.core.shoplocation.model.saveaddress;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveAddress {

    @SerializedName("is_success")
    @Expose
    private Integer isSuccess;
    @SerializedName("addr_id")
    @Expose
    private String addrId;


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

    /**
     *
     * @return
     *     The addrId
     */
    public String getAddrId() {
        return addrId;
    }

    /**
     *
     * @param addrId
     *     The addr_id
     */
    public void setAddrId(String addrId) {
        this.addrId = addrId;
    }

}
