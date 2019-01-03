package com.tokopedia.transactiondata.entity.response.cod;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fajarnuha on 03/01/19.
 */
public class ValidateCheckoutCod {

    @SerializedName("header")
    @Expose
    private Header header;
    @SerializedName("data")
    @Expose
    private DataStatus data;
    @SerializedName("status")
    @Expose
    private String status;

    public ValidateCheckoutCod() {
    }

    public ValidateCheckoutCod(Header header, DataStatus data, String status) {
        this.header = header;
        this.data = data;
        this.status = status;
    }

    public DataStatus getData() {
        return data;
    }

    public void setData(DataStatus data) {
        this.data = data;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
