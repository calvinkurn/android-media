package com.tokopedia.posapp.data.pojo.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by okasurya on 10/11/17.
 */

public class PaymentStatusResponse {
    @SerializedName("success")
    @Expose
    private int sucess;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<PaymentStatus> data;

    public int getSucess() {
        return sucess;
    }

    public void setSucess(int sucess) {
        this.sucess = sucess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PaymentStatus> getData() {
        return data;
    }

    public void setData(List<PaymentStatus> data) {
        this.data = data;
    }
}
