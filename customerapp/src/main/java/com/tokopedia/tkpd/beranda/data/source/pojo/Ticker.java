package com.tokopedia.tkpd.beranda.data.source.pojo;

import com.google.gson.annotations.Expose;

/**
 * Created by henrypriyono on 26/01/18.
 */

public class Ticker {
    @Expose
    private Tickers[] tickers;

    @Expose
    private Meta meta;

    public class Tickers {

        @Expose
        private String message;

        @Expose
        private String id;

        @Expose
        private String title;

        @Expose
        private String color;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    public class Meta {
        @Expose
        private String total_data;

        public String getTotal_data() {
            return total_data;
        }

        public void setTotal_data(String total_data) {
            this.total_data = total_data;
        }
    }
}
