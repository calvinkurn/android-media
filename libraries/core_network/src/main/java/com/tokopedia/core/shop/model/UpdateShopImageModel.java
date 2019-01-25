package com.tokopedia.core.shop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Toped10 on 5/26/2016.
 */
@Parcel
public class UpdateShopImageModel {

    /**
     * status : OK
     * config : null
     * data : {"is_success":1}
     * server_process_time : 0.034189
     */
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("config")
    @Expose
    String config;
    /**
     * is_success : 1
     */
    @SerializedName("data")
    @Expose
    DataBean data;
    @SerializedName("server_process_time")
    @Expose
    String server_process_time;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getServer_process_time() {
        return server_process_time;
    }

    public void setServer_process_time(String server_process_time) {
        this.server_process_time = server_process_time;
    }

    @Parcel
    public static class DataBean {
        @SerializedName("is_success")
        @Expose
        int is_success;

        public int getIs_success() {
            return is_success;
        }

        public void setIs_success(int is_success) {
            this.is_success = is_success;
        }
    }
}
