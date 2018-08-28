package com.tokopedia.reksadana.view.data.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.reksadana.view.data.common.Result;

public class DashBoardData {
    @Expose
    @SerializedName("user_id")
    public String user_id;
    @Expose
    @SerializedName("phone")
    public String phone;
    @Expose
    @SerializedName("email")
    public String email;
    @Expose
    @SerializedName("name")
    public String name;
    @Expose
    @SerializedName("signature_image_url")
    public String signature_image_url;
    @Expose
    @SerializedName("info_id")
    public String info_id;
    @Expose
    @SerializedName("autosweep")
    public String autosweep;
    @Expose
    @SerializedName("mf_application_status")
    public String mf_application_status;
    @Expose
    @SerializedName("applied_time")
    public String applied_time;
    @Expose
    @SerializedName("approved_time")
    public String approved_time;
    @Expose
    @SerializedName("result")
    public Result result;

    public DashBoardData(String user_id, String phone, String email, String name, String signature_image_url, String info_id, String autosweep, String mf_application_status, String applied_time, String approved_time, Result result) {
        this.user_id = user_id;
        this.phone = phone;
        this.email = email;
        this.name = name;
        this.signature_image_url = signature_image_url;
        this.info_id = info_id;
        this.autosweep = autosweep;
        this.mf_application_status = mf_application_status;
        this.applied_time = applied_time;
        this.approved_time = approved_time;
        this.result = result;
    }

    public String userId() {
        return user_id;
    }

    public String phone() {
        return phone;
    }

    public String email() {
        return email;
    }

    public String name() {
        return name;
    }

    public String signatureImageUrl() {
        return signature_image_url;
    }

    public String infoId() {
        return info_id;
    }

    public String autosweep() {
        return autosweep;
    }

    public String mfApplicationStatus() {
        return mf_application_status;
    }

    public String appliedTime() {
        return applied_time;
    }

    public String approvedTime() {
        return approved_time;
    }

    public Result result() {
        return result;
    }
}
