package com.tokopedia.posapp.base.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author okasurya on 4/2/18.
 */

public class PosListResponse<T> {
    @SerializedName("header")
    @Expose
    private Header header;
    @SerializedName(value="data")
    @Expose
    private List<T> data;

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

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

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
