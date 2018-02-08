package com.tokopedia.core.network.entity.home;

import android.text.Html;
import android.text.Spanned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.shop.model.etalasemodel.List;
import com.tokopedia.core.util.MethodChecker;

import java.util.ArrayList;
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
        ArrayList<Tickers> tickers;

        public ArrayList<Tickers> getTickers() {
            return tickers;
        }

        public void setTickers(ArrayList<Tickers> tickers) {
            this.tickers = tickers;
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
                    ", title='" + title + '\'' +
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
