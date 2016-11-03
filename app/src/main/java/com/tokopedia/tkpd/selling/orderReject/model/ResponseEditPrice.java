package com.tokopedia.tkpd.selling.orderReject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Toped10 on 6/9/2016.
 */
@Parcel
public class ResponseEditPrice {
    /**
     * status : OK
     * data : {"is_success":0}
     * server_process_time : 0.034775
     * config : null
     * message_error : ["Harga tidak benar.","Harga tidak benar.","Maaf, Permohonan Anda tidak dapat diproses saat ini. Mohon dicoba kembali."]
     */

    @SerializedName("status")
    @Expose
    String status;
    /**
     * is_success : 0
     */

    @SerializedName("data")
    @Expose
    DataBean data;

    @SerializedName("server_process_time")
    @Expose
    String server_process_time;

    @SerializedName("config")
    @Expose
    String config;

    @SerializedName("message_error")
    @Expose
    List<String> message_error;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public List<String> getMessage_error() {
        return message_error;
    }

    public void setMessage_error(List<String> message_error) {
        this.message_error = message_error;
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
