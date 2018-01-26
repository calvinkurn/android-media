package com.tokopedia.tkpd.beranda.data.source.pojo;

import com.google.gson.annotations.Expose;

/**
 * Created by henrypriyono on 26/01/18.
 */

public class DynamicHomeChannel {
    @Expose
    private Channels[] channels;

    public Channels[] getChannels() {
        return channels;
    }

    public void setChannels(Channels[] channels) {
        this.channels = channels;
    }

    public class Channels {
        @Expose
        private String id;

        @Expose
        private String layout;

        @Expose
        private String name;

        @Expose
        private Grid[] grid;

        @Expose
        private String type;

        @Expose
        private Header header;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLayout() {
            return layout;
        }

        public void setLayout(String layout) {
            this.layout = layout;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Grid[] getGrid() {
            return grid;
        }

        public void setGrid(Grid[] grid) {
            this.grid = grid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Header getHeader() {
            return header;
        }

        public void setHeader(Header header) {
            this.header = header;
        }
    }

    public class Grid {
        @Expose
        private String id;

        @Expose
        private String price;

        @Expose
        private String imageUrl;

        @Expose
        private String name;

        @Expose
        private String applink;

        @Expose
        private String url;

        @Expose
        private String discount;

        @Expose
        private String slashedPrice;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getApplink() {
            return applink;
        }

        public void setApplink(String applink) {
            this.applink = applink;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getSlashedPrice() {
            return slashedPrice;
        }

        public void setSlashedPrice(String slashedPrice) {
            this.slashedPrice = slashedPrice;
        }
    }

    public class Header {
        @Expose
        private String id;

        @Expose
        private String name;

        @Expose
        private String expiredTime;

        @Expose
        private String applink;

        @Expose
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getExpiredTime() {
            return expiredTime;
        }

        public void setExpiredTime(String expiredTime) {
            this.expiredTime = expiredTime;
        }

        public String getApplink() {
            return applink;
        }

        public void setApplink(String applink) {
            this.applink = applink;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
