package com.tkpd.library.utils.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.database.model.NetworkModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.normansyah on 2/11/16.
 */
@Deprecated
public class ListShippingCity {
    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("data")
    @Expose
    Data data;

    @SerializedName("config")
    @Expose
    String config;

    @SerializedName("server_process_time")
    @Expose
    String server_process_time;

    @SerializedName("message_error")
    @Expose
    List<String> messageError;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getServer_process_time() {
        return server_process_time;
    }

    public void setServer_process_time(String server_process_time) {
        this.server_process_time = server_process_time;
    }

    public List<String> getMessageError() {
        return messageError;
    }

    public void setMessageError(List<String> messageError) {
        this.messageError = messageError;
    }

    public static class Data {
        @SerializedName("shipping_city")
        @Expose
        ArrayList<Shipping_city> shippingCity;

        public ArrayList<Shipping_city> getShippingCity() {
            return shippingCity;
        }

        public void setShippingCity(ArrayList<Shipping_city> shippingCity) {
            this.shippingCity = shippingCity;
        }
    }

    public static class Shipping_city extends NetworkModel {
        @SerializedName("district_name")
        @Expose
        String districtName;

        @SerializedName("district_id")
        @Expose
        String districtId;

        public String getDistrictName() {
            return districtName;
        }

        public void setDistrictName(String districtName) {
            this.districtName = districtName;
        }

        public String getDistrictId() {
            return districtId;
        }

        public void setDistrictId(String districtId) {
            this.districtId = districtId;
        }
    }
}
