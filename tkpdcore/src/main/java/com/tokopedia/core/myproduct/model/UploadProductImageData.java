package com.tokopedia.core.myproduct.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by m.normansyah on 04/12/2015.
 */
@Parcel
public class UploadProductImageData {

    /**
     * this is for parcelable
     */
    public UploadProductImageData(){}

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("server_process_time")
    @Expose
    String serverProcessTime;

    @SerializedName("message_error")
    @Expose
    ArrayList<String> messageError;

//    @SerializedName("result")// old ws-new
    @SerializedName("data")// v4
    @Expose
    Result result;

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

    public ArrayList<String> getMessageError() {
        return messageError;
    }

    public void setMessageError(ArrayList<String> messageError) {
        this.messageError = messageError;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "UploadProductImageData{" +
                "status='" + status + '\'' +
                ", serverProcessTime=" + serverProcessTime +
                ", messageError='" + messageError + '\'' +
                ", result=" + result +
                '}';
    }

    @Parcel
    public static class Result{

        @SerializedName("file_path")
        @Expose
        String filePath;

        @SerializedName("file_th")
        @Expose
        String fileThumbnail;

        @SerializedName("pic_obj")
        @Expose
        String picObj;

        @SerializedName("message_status")
        @Expose
        String messageStatus;

        @SerializedName("src")
        @Expose
        String src;

        @SerializedName("success")
        @Expose
        int success;

        /**
         * this is for parcelable
         */
        public Result(){}

        public String getPicObj() {
            return picObj;
        }

        public void setPicObj(String picObj) {
            this.picObj = picObj;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getFileThumbnail() {
            return fileThumbnail;
        }

        public void setFileThumbnail(String fileThumbnail) {
            this.fileThumbnail = fileThumbnail;
        }

        public String getMessageStatus() {
            return messageStatus;
        }

        public void setMessageStatus(String messageStatus) {
            this.messageStatus = messageStatus;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public int getSuccess() {
            return success;
        }

        public void setSuccess(int success) {
            this.success = success;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "filePath='" + filePath + '\'' +
                    ", fileThumbnail='" + fileThumbnail + '\'' +
                    ", picObj='" + picObj + '\'' +
                    ", messageStatus='" + messageStatus + '\'' +
                    ", src='" + src + '\'' +
                    ", success=" + success +
                    '}';
        }
    }



}
