package com.tokopedia.tkpd.beranda.domain.model.banner;

import com.google.gson.annotations.SerializedName;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomeBannerResponseModel {


    @SerializedName("meta")
    private BannerMetaData meta;
    @SerializedName("data")
    private BannerDataModel data;
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

    public BannerMetaData getMeta() {
        return meta;
    }

    public void setMeta(BannerMetaData meta) {
        this.meta = meta;
    }

    public BannerDataModel getData() {
        return data;
    }

    public void setData(BannerDataModel data) {
        this.data = data;
    }

}
