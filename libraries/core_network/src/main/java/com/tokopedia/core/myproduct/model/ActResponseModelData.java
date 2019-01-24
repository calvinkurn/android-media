package com.tokopedia.core.myproduct.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by noiz354 on 6/2/16.
 */
@Parcel
public class ActResponseModelData {

    @SerializedName("is_success")
    @Expose
    int isSuccess;


    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    @Override
    public String toString() {
        return "ForgotPasswordModel{" +
                ", isSuccess=" + isSuccess +
                '}';
    }

}

