package com.tokopedia.core.network.entity.discovery;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.discovery.model.ObjContainer;

/**
 * Created by brilliant.oka on 12/05/17.
 */

@Deprecated
public class BannerOfficialStoreModel {
    @SerializedName("banner_url")
    @Expose
    private String bannerUrl;

    @SerializedName("process-time ")
    @Expose
    private String processTime;

    @SerializedName("shop_url")
    @Expose
    private String shopUrl;

    @SerializedName("status")
    @Expose
    private String status;

    private String keyword;

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * use this for listener
     */
    public static final class BannerOfficialStoreContainer implements ObjContainer<BannerOfficialStoreModel> {

        BannerOfficialStoreModel model;

        public BannerOfficialStoreContainer(BannerOfficialStoreModel model) {
            this.model = model;
        }

        @Override
        public BannerOfficialStoreModel body() {
            return model;
        }
    }
}
