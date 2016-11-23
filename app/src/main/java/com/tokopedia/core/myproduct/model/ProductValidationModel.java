package com.tokopedia.core.myproduct.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Arrays;

/**
 * Created by m.normansyah on 29/12/2015.
 */
@Parcel(parcelsIndex = false)
public class ProductValidationModel {

    /**
     * this is for parcelable
     */
    public ProductValidationModel(){}

    @SerializedName("message_error")
    @Expose
    String[] message_error;

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

    public String[] getMessage_error() {
        return message_error;
    }

    public void setMessage_error(String[] message_error) {
        this.message_error = message_error;
    }

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

    @Parcel(parcelsIndex = false)
    public static class Data{

        /**
         * this is for parcelable
         */
        public Data(){}

        @SerializedName("post_key")
        @Expose
        String postKey;

        public String getPostKey() {
            return postKey;
        }

        public void setPostKey(String postKey) {
            this.postKey = postKey;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "postKey='" + postKey + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ProductValidationModel{" +
                "message_error=" + Arrays.toString(message_error) +
                ", status='" + status + '\'' +
                ", data=" + data +
                ", config='" + config + '\'' +
                ", server_process_time='" + server_process_time + '\'' +
                '}';
    }
}
