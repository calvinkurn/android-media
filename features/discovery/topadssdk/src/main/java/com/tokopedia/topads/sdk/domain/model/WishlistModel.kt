package com.tokopedia.topads.sdk.domain.model;

import com.google.gson.annotations.SerializedName;

/**
 * Author errysuprayogi on 04,December,2018
 */
public class WishlistModel {

    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        @SerializedName("success")
        private boolean success;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}
