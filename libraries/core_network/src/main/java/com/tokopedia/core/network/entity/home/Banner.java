package com.tokopedia.core.network.entity.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by m.normansyah on 2/24/16.
 * modified by mady for supporting new banner response
 */
@Deprecated
public class Banner {

    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("data")
    @Expose
    private Data data;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Meta {
        @SerializedName("total_data")
        @Expose
        private int total_data;

        public int getTotal_data() {
            return total_data;
        }

        public void setTotal_data(int total_data) {
            this.total_data = total_data;
        }
    }

    public static class Data {
        @SerializedName("slides")
        @Expose
        private List<Slide> slides;

        public List<Slide> getSlides() {
            return slides;
        }

        public void setSlides(List<Slide> slides) {
            this.slides = slides;
        }

        public static class Slide {
            @SerializedName("id")
            @Expose
            private int id;
            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("message")
            @Expose
            private String message;
            @SerializedName("image_url")
            @Expose
            private String image_url;
            @SerializedName("redirect_url")
            @Expose
            private String redirect_url;
            @SerializedName("target")
            @Expose
            private int target;
            @SerializedName("device")
            @Expose
            private int device;
            @SerializedName("expire_time")
            @Expose
            private String expire_time;
            @SerializedName("state")
            @Expose
            private int state;
            @SerializedName("created_by")
            @Expose
            private int created_by;
            @SerializedName("created_on")
            @Expose
            private String created_on;
            @SerializedName("updated_by")
            @Expose
            private int updated_by;
            @SerializedName("updated_on")
            @Expose
            private String updated_on;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getImage_url() {
                return image_url;
            }

            public void setImage_url(String image_url) {
                this.image_url = image_url;
            }

            public String getRedirect_url() {
                return redirect_url;
            }

            public void setRedirect_url(String redirect_url) {
                this.redirect_url = redirect_url;
            }

            public int getTarget() {
                return target;
            }

            public void setTarget(int target) {
                this.target = target;
            }

            public int getDevice() {
                return device;
            }

            public void setDevice(int device) {
                this.device = device;
            }

            public String getExpire_time() {
                return expire_time;
            }

            public void setExpire_time(String expire_time) {
                this.expire_time = expire_time;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public int getCreated_by() {
                return created_by;
            }

            public void setCreated_by(int created_by) {
                this.created_by = created_by;
            }

            public String getCreated_on() {
                return created_on;
            }

            public void setCreated_on(String created_on) {
                this.created_on = created_on;
            }

            public int getUpdated_by() {
                return updated_by;
            }

            public void setUpdated_by(int updated_by) {
                this.updated_by = updated_by;
            }

            public String getUpdated_on() {
                return updated_on;
            }

            public void setUpdated_on(String updated_on) {
                this.updated_on = updated_on;
            }
        }
    }

}
