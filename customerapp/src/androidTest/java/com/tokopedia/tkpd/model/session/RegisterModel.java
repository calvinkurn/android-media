package com.tokopedia.tkpd.model.session;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.tkpd.model.ErrorMessageModel;

public class RegisterModel extends ErrorMessageModel {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        @SerializedName("u_id")
        @Expose
        private long uId;
        @SerializedName("is_active")
        @Expose
        private long isActive;
        @SerializedName("action")
        @Expose
        private long action;
        @SerializedName("is_success")
        @Expose
        private long isSuccess;

        public long getUId() {
            return uId;
        }

        public void setUId(long uId) {
            this.uId = uId;
        }

        public long getIsActive() {
            return isActive;
        }

        public void setIsActive(long isActive) {
            this.isActive = isActive;
        }

        public long getAction() {
            return action;
        }

        public void setAction(long action) {
            this.action = action;
        }

        public long getIsSuccess() {
            return isSuccess;
        }

        public void setIsSuccess(long isSuccess) {
            this.isSuccess = isSuccess;
        }

    }
}

