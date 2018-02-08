package com.tokopedia.tkpd.beranda.domain.model.brands;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.database.manager.GlobalCacheManager;

import java.util.List;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class BrandsOfficialStoreResponseModel {

    @SerializedName("status")
    private String status;
    @SerializedName("process-time")
    private String processtime;
    @SerializedName("data")
    private List<BrandDataModel> data;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProcesstime() {
        return processtime;
    }

    public void setProcesstime(String processtime) {
        this.processtime = processtime;
    }

    public List<BrandDataModel> getData() {
        return data;
    }

    public void setData(List<BrandDataModel> data) {
        this.data = data;
    }

}
