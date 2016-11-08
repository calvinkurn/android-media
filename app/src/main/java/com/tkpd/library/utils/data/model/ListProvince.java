package com.tkpd.library.utils.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.database.model.NetworkModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noiz354 on 2/2/16.
 */
public class ListProvince {
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

    public class Data {
        @SerializedName("provinces")
        @Expose
        ArrayList<Province> provinces;

        public ArrayList<Province> getProvinces() {
            return provinces;
        }

        public void setProvinces(ArrayList<Province> provinces) {
            this.provinces = provinces;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "provinces=" + provinces +
                    '}';
        }
    }

    public static class Province extends NetworkModel {
        @SerializedName("province_name")
        @Expose
        String provinceName;

        @SerializedName("province_id")
        @Expose
        String provinceId;

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public String getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(String provinceId) {
            this.provinceId = provinceId;
        }

        @Override
        public String toString() {
            return "Province{" +
                    "provinceName='" + provinceName + '\'' +
                    ", provinceId='" + provinceId + '\'' +
                    '}';
        }
    }
}
