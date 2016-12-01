package com.tokopedia.core.rescenter.detail.model.actionresponsedata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hangnadi on 3/1/16.
 */
public class UploadResCenterImageData {

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
        String fileUrl;

        @SerializedName("file_th")
        @Expose
        String fileThUrl;

        @SerializedName("is_success")
        @Expose
        int success;

        @SerializedName("file_uploaded")
        @Expose
        String fileUploaded;

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public String getFileThUrl() {
            return fileThUrl;
        }

        public void setFileThUrl(String fileThUrl) {
            this.fileThUrl = fileThUrl;
        }

        public boolean isSuccess() {
            return success == 1;
        }

        public void setSuccess(int success) {
            this.success = success;
        }

        public String getFileUploaded() {
            return fileUploaded;
        }

        public void setFileUploaded(String fileUploaded) {
            this.fileUploaded = fileUploaded;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "file_path='" + fileUrl + '\'' +
                    ", file_th='" + fileThUrl + '\'' +
                    ", success=" + success + '\'' +
                    "file_uploaded" + fileUploaded +
                    '}';
        }
    }


}
