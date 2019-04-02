package com.tokopedia.core.common.ticker.model;

import android.text.Spanned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.MethodChecker;

import java.util.Arrays;

/**
 * Created by Nisie on 7/15/16.
 */
public class Ticker {
    @SerializedName("data")
    @Expose
    Data data;

    @SerializedName("meta")
    @Expose
    Meta meta;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return "Ticker{" +
                "data=" + data +
                ", meta=" + meta +
                '}';
    }

    public static class Data {
        @SerializedName("tickers")
        @Expose
        Tickers[] tickers;

        public Tickers[] getTickers() {
            return tickers;
        }

        public void setTickers(Tickers[] tickers) {
            this.tickers = tickers;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "tickers=" + Arrays.toString(tickers) +
                    '}';
        }
    }

    public static class Tickers {
        @SerializedName("redirect_url")
        @Expose
        String redirectUrl;

        @SerializedName("created_by")
        @Expose
        String createdBy;

        @SerializedName("created_on")
        @Expose
        String createdOn;

        @SerializedName("state")
        @Expose
        String state;

        @SerializedName("expire_time")
        @Expose
        String expireTime;

        @SerializedName("id")
        @Expose
        String id;

        @SerializedName("message")
        @Expose
        String message;

        @SerializedName("title")
        @Expose
        String title;

        @SerializedName("target")
        @Expose
        String target;

        @SerializedName("device")
        @Expose
        String device;

        @SerializedName("updated_on")
        @Expose
        String updatedOn;

        @SerializedName("updated_by")
        @Expose
        String updatedBy;

        @SerializedName("color")
        @Expose
        private String color;

        public String getRedirectUrl() {
            return redirectUrl;
        }

        public void setRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(String expireTime) {
            this.expireTime = expireTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Spanned getMessage() {
            return MethodChecker.fromHtml(message);
        }

        public String getBasicMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

        public String getUpdatedOn() {
            return updatedOn;
        }

        public void setUpdatedOn(String updatedOn) {
            this.updatedOn = updatedOn;
        }

        public String getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
        }

        @Override
        public String toString() {
            return "tickers{" +
                    "redirectUrl='" + redirectUrl + '\'' +
                    ", createdBy='" + createdBy + '\'' +
                    ", createdOn='" + createdOn + '\'' +
                    ", state='" + state + '\'' +
                    ", expireTime='" + expireTime + '\'' +
                    ", id='" + id + '\'' +
                    ", message='" + message + '\'' +
                    ", title='" + title + '\'' +
                    ", target='" + target + '\'' +
                    ", device='" + device + '\'' +
                    ", updatedOn='" + updatedOn + '\'' +
                    ", updatedBy='" + updatedBy + '\'' +
                    '}';
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    public static class Meta {
        @SerializedName("total_data")
        @Expose
        String totalData;

        public String getTotalData() {
            return totalData;
        }

        public void setTotalData(String totalData) {
            this.totalData = totalData;
        }

        @Override
        public String toString() {
            return "Meta{" +
                    "totalData='" + totalData + '\'' +
                    '}';
        }
    }
}
