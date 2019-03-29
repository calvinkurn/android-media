package com.tokopedia.core.shop.model.responseEdit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Toped10 on 5/25/2016.
 */

@Deprecated
@Parcel
public class ResponseEdit {
    /**
     * status : OK
     * message_status : ["Anda telah sukses memperbaharui informasi toko."]
     * config : null
     * data : {"is_success":1}
     * server_process_time : 0.073405
     */

    @SerializedName("status")
    @Expose
    String status;

    /**
     * is_success : 1
     */
    @SerializedName("data")
    @Expose
    DataBean data;
    @SerializedName("server_process_time")
    @Expose
    String server_process_time;
    @SerializedName("message_status")
    @Expose
    List<String> message_status;

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

    public List<String> getMessage_status() {
        return message_status;
    }

    public void setMessage_status(List<String> message_status) {
        this.message_status = message_status;
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
