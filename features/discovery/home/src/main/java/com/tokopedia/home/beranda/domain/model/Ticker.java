package com.tokopedia.home.beranda.domain.model;

import android.text.Spanned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;

import java.util.ArrayList;

/**
 * Created by Nisie on 7/15/16.
 */
public class Ticker {
    @SerializedName("tickers")
    @Expose
    ArrayList<Tickers> tickers;

    public ArrayList<Tickers> getTickers() {
        return tickers;
    }

    public void setTickers(ArrayList<Tickers> tickers) {
        this.tickers = tickers;
    }

    @Override
    public String toString() {
        return "Ticker{" +
                "data=" + tickers +
                '}';
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

        @SerializedName("expire_time")
        @Expose
        String expireTime;

        @SerializedName("id")
        @Expose
        String id;

        @SerializedName("message")
        @Expose
        String message;

        @SerializedName("layout")
        @Expose
        String layout;

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

        @SerializedName("ticker_type")
        @Expose
        String ticker_type;

        @SerializedName("color")
        @Expose
        String color;

        @SerializedName("status")
        @Expose
        String status;

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
            String str = message.replaceAll("<p>(.*?)</p>", "$1");
            return MethodChecker.fromHtml(str);
        }

        public String getMessage2() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getLayout() { return layout; }

        public void setLayout(String layout) { this.layout = layout; }

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

        public String getTickerType() {
            return ticker_type;
        }

        public void setTickerType(String ticker_type) {
            this.ticker_type = ticker_type;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "Tickers{" +
                    "redirectUrl='" + redirectUrl + '\'' +
                    ", createdBy='" + createdBy + '\'' +
                    ", createdOn='" + createdOn + '\'' +
                    ", expireTime='" + expireTime + '\'' +
                    ", id='" + id + '\'' +
                    ", message='" + message + '\'' +
                    ", layout='" + layout + '\'' +
                    ", target='" + target + '\'' +
                    ", device='" + device + '\'' +
                    ", updatedOn='" + updatedOn + '\'' +
                    ", updatedBy='" + updatedBy + '\'' +
                    ", ticker_type='" + ticker_type + '\'' +
                    ", color='" + color + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }
    }
}
