package com.tokopedia.core.myproduct.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by m.normansyah on 3/10/16.
 */
public class ImageUploadModelWsNew {
    @SerializedName("result")
    Result result;

    @SerializedName("status")
    String status;

    @SerializedName("message_error")
    String messageError[];

    @SerializedName("server_process_time")
    String serverProcessTime;


    public class Result {
        @SerializedName("message_status")
        String messageStatus;

        @SerializedName("src")
        String src;

        public static final int SUCCESS_MESSAGE = 1;

        @SerializedName("success")
        String success;

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

        public int getIntSuccess(){
            return Integer.parseInt(success);
        }

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getMessageError() {
        return messageError;
    }

    public void setMessageError(String[] messageError) {
        this.messageError = messageError;
    }

    public String getServerProcessTime() {
        return serverProcessTime;
    }

    public void setServerProcessTime(String serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }
}
