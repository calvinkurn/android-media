package com.tokopedia.core.myproduct.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by m.normansyah on 18/12/2015.
 */
@Parcel
public class GenerateHostModel {

    /**
     * This is for parcelable
     */
    public GenerateHostModel(){}

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("message_error")
    @Expose
    List<String> messageError;

    @SerializedName("data")
    @Expose
    Data data;

    @SerializedName("config")
    @Expose
    String config;

    @SerializedName("server_process_time")
    @Expose
    String server_process_time;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Parcel
    public static class Data{

        /**
         * This is for parcelable
         */
        public Data(){}

        @SerializedName("generated_host")
        @Expose
        GenerateHost generateHost;

        public GenerateHost getGenerateHost() {
            return generateHost;
        }

        public void setGenerateHost(GenerateHost generateHost) {
            this.generateHost = generateHost;
        }
    }

    @Parcel
    public static class GenerateHost{

        /**
         * This is for parcelable
         */
        public GenerateHost(){}

        @SerializedName("upload_host")
        @Expose
        String uploadHost;

        @SerializedName("server_id")
        @Expose
        String serverId;

        @SerializedName("user_id")
        @Expose
        String userId;

        public String getUploadHost() {
            return uploadHost;
        }

        public void setUploadHost(String uploadHost) {
            this.uploadHost = uploadHost;
        }

        public String getServerId() {
            return serverId;
        }

        public void setServerId(String serverId) {
            this.serverId = serverId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "GenerateHost{" +
                    "uploadHost='" + uploadHost + '\'' +
                    ", serverId='" + serverId + '\'' +
                    ", userId='" + userId + '\'' +
                    '}';
        }
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }



    public List<String> getMessageError() {
        return messageError;
    }

    public void setMessageError(List<String> messageError) {
        this.messageError = messageError;
    }

    public String getServer_process_time() {
        return server_process_time;
    }

    public void setServer_process_time(String server_process_time) {
        this.server_process_time = server_process_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
