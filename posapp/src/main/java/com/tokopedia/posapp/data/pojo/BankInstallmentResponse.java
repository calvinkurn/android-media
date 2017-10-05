package com.tokopedia.posapp.data.pojo;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.posapp.domain.model.bank.BankDomain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by okasurya on 01/12/2015.
 */
public class BankInstallmentResponse {
    @SerializedName("success")
    @Expose
    private String sucess;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private BankListResponse data;

    public String getSucess() {
        return sucess;
    }

    public void setSucess(String sucess) {
        this.sucess = sucess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BankListResponse getData() {
        return data;
    }

    public void setData(BankListResponse data) {
        this.data = data;
    }
}
