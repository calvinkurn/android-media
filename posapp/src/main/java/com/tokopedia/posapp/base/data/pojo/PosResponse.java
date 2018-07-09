package com.tokopedia.posapp.base.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;

/**
 * Created by okasurya on 9/19/17.
 */

public class PosResponse<T> extends DataResponse<T> {
    @SerializedName("success")
    @Expose
    private String sucess;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("message")
    @Expose
    private String message;

    public String getSucess() {
        return sucess;
    }

    public void setSucess(String sucess) {
        this.sucess = sucess;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
