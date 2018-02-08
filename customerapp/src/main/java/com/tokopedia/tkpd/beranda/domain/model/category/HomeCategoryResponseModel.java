package com.tokopedia.tkpd.beranda.domain.model.category;

import com.google.gson.annotations.SerializedName;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomeCategoryResponseModel {


    @SerializedName("headers")
    private HeaderResponseModel headers;
    @SerializedName("data")
    private CategoryDataModel data;
    private boolean isSuccess;
    private long expiredTime = 0;


    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public HeaderResponseModel getHeaders() {
        return headers;
    }

    public void setHeaders(HeaderResponseModel headers) {
        this.headers = headers;
    }

    public CategoryDataModel getData() {
        return data;
    }

    public void setData(CategoryDataModel data) {
        this.data = data;
    }

}
