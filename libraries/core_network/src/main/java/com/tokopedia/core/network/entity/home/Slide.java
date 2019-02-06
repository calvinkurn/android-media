package com.tokopedia.core.network.entity.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by m.normansyah on 2/24/16.
 */

@Deprecated
public class Slide {
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
        return "Slide{" +
                "data=" + data +
                ", meta=" + meta +
                '}';
    }

    public static class Data {
        @SerializedName("slides")
        @Expose
        Slides[] slides;

        public Slides[] getSlides() {
            return slides;
        }

        public void setSlides(Slides[] slides) {
            this.slides = slides;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "slides=" + Arrays.toString(slides) +
                    '}';
        }
    }

    public static class Slides {
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

        @SerializedName("image_url")
        @Expose
        String imageUrl;

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

        @SerializedName("applink")
        String applink;

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

        public String getMessage() {
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

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
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

        public String getApplink() {
            return applink;
        }

        public void setApplink(String applink) {
            this.applink = applink;
        }

        @Override
        public String toString() {
            return "Slides{" +
                    "redirectUrl='" + redirectUrl + '\'' +
                    ", createdBy='" + createdBy + '\'' +
                    ", createdOn='" + createdOn + '\'' +
                    ", state='" + state + '\'' +
                    ", expireTime='" + expireTime + '\'' +
                    ", id='" + id + '\'' +
                    ", message='" + message + '\'' +
                    ", title='" + title + '\'' +
                    ", imageUrl='" + imageUrl + '\'' +
                    ", target='" + target + '\'' +
                    ", device='" + device + '\'' +
                    ", updatedOn='" + updatedOn + '\'' +
                    ", updatedBy='" + updatedBy + '\'' +
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
