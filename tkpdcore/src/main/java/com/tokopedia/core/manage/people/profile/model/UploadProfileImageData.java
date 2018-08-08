package com.tokopedia.core.manage.people.profile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 6/29/16.
 */
public class UploadProfileImageData {

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("server_process_time")
    @Expose
    String serverProcessTime;

    @SerializedName("message_error")
    @Expose
    List<String> messageError;

    @SerializedName("data")
    @Expose
    Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServerProcessTime() {
        return serverProcessTime;
    }

    public void setServerProcessTime(String serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    public List<String> getMessageError() {
        return messageError;
    }

    public void setMessageError(List<String> messageError) {
        this.messageError = messageError;
    }


    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UploadResCenterImageData{" +
                "status='" + status + '\'' +
                ", serverProcessTime=" + serverProcessTime +
                ", messageError='" + messageError + '\'' +
                ", result=" + data +
                '}';
    }

    public static class Data {

        @SerializedName("file_path")
        @Expose
        private String filePath;
        @SerializedName("pic_obj")
        @Expose
        private String picObj;
        @SerializedName("file_th")
        @Expose
        private String fileTh;
        @Expose
        private int success;

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getPicObj() {
            return picObj;
        }

        public void setPicObj(String picObj) {
            this.picObj = picObj;
        }

        public String getFileTh() {
            return fileTh;
        }

        public void setFileTh(String fileTh) {
            this.fileTh = fileTh;
        }

        public int getSuccess() {
            return success;
        }

        public boolean isSuccess() {
            return getSuccess() == 1;
        }

        public void setSuccess(int success) {
            this.success = success;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "file_path='" + filePath + '\'' +
                    ", file_th='" + fileTh + '\'' +
                    ", pic_obj=" + picObj + '\'' +
                    ", success=" + success +
                    '}';
        }
    }
}
