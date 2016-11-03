package com.tokopedia.tkpd.session.model.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by m.normansyah on 1/25/16.
 */
public class ValidateEmailData {
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

    public class Data {
        public static final int EMAIL_STATUS_NOT_REGISTERED = 0;
        public static final int EMAIL_STATUS_REGISTERED = 1;

        @SerializedName("email_status")
        @Expose
        String email_status;

        public String getEmail_status() {
            return email_status;
        }

        public void setEmail_status(String email_status) {
            this.email_status = email_status;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "email_status='" + email_status + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ValidateEmailData{" +
                "status='" + status + '\'' +
                ", data=" + data +
                ", config='" + config + '\'' +
                ", server_process_time='" + server_process_time + '\'' +
                '}';
    }
}
