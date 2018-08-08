package com.tokopedia.gm.featured.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class GMFeaturedProductDataModel {
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public static class Label {

        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("color")
        @Expose
        private String color;

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

    public static class WholesaleDetail {

        @SerializedName("Qty")
        @Expose
        private String qty;
        @SerializedName("Price")
        @Expose
        private String price;

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

    }

    public static class Datum {

        @SerializedName("product_id")
        @Expose
        private long productId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("uri")
        @Expose
        private String uri;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("image_uri")
        @Expose
        private String imageUri;
        @SerializedName("preorder")
        @Expose
        private boolean preorder;
        @SerializedName("returnable")
        @Expose
        private boolean returnable;
        @SerializedName("wholesale")
        @Expose
        private boolean wholesale;
        @SerializedName("wholesale_detail")
        @Expose
        private List<WholesaleDetail> wholesaleDetail = null;
        @SerializedName("cashback")
        @Expose
        private boolean cashback;
        @SerializedName("labels")
        @Expose
        private List<Label> labels = null;
        @SerializedName("cashback_detail")
        @Expose
        private CashbackDetail cashbackDetail;
        @SerializedName("badges")
        @Expose
        private List<Badge> badges = null;

        public long getProductId() {
            return productId;
        }

        public void setProductId(long productId) {
            this.productId = productId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getImageUri() {
            return imageUri;
        }

        public void setImageUri(String imageUri) {
            this.imageUri = imageUri;
        }

        public boolean isPreorder() {
            return preorder;
        }

        public void setPreorder(boolean preorder) {
            this.preorder = preorder;
        }

        public boolean isReturnable() {
            return returnable;
        }

        public void setReturnable(boolean returnable) {
            this.returnable = returnable;
        }

        public boolean isWholesale() {
            return wholesale;
        }

        public void setWholesale(boolean wholesale) {
            this.wholesale = wholesale;
        }

        public List<WholesaleDetail> getWholesaleDetail() {
            return wholesaleDetail;
        }

        public void setWholesaleDetail(List<WholesaleDetail> wholesaleDetail) {
            this.wholesaleDetail = wholesaleDetail;
        }

        public boolean isCashback() {
            return cashback;
        }

        public void setCashback(boolean cashback) {
            this.cashback = cashback;
        }

        public List<Label> getLabels() {
            return labels;
        }

        public void setLabels(List<Label> labels) {
            this.labels = labels;
        }

        public CashbackDetail getCashbackDetail() {
            return cashbackDetail;
        }

        public void setCashbackDetail(CashbackDetail cashbackDetail) {
            this.cashbackDetail = cashbackDetail;
        }

        public List<Badge> getBadges() {
            return badges;
        }

        public void setBadges(List<Badge> badges) {
            this.badges = badges;
        }

    }

    public static class Badge {

        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("image_url")
        @Expose
        private String imageUrl;

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

    }

    public static class CashbackDetail {

        @SerializedName("cashback_status")
        @Expose
        private long cashbackStatus;
        @SerializedName("cashback_percent")
        @Expose
        private long cashbackPercent;
        @SerializedName("is_cashback_expired")
        @Expose
        private long isCashbackExpired;
        @SerializedName("cashback_value")
        @Expose
        private long cashbackValue;

        public long getCashbackStatus() {
            return cashbackStatus;
        }

        public void setCashbackStatus(long cashbackStatus) {
            this.cashbackStatus = cashbackStatus;
        }

        public long getCashbackPercent() {
            return cashbackPercent;
        }

        public void setCashbackPercent(long cashbackPercent) {
            this.cashbackPercent = cashbackPercent;
        }

        public long getIsCashbackExpired() {
            return isCashbackExpired;
        }

        public void setIsCashbackExpired(long isCashbackExpired) {
            this.isCashbackExpired = isCashbackExpired;
        }

        public long getCashbackValue() {
            return cashbackValue;
        }

        public void setCashbackValue(long cashbackValue) {
            this.cashbackValue = cashbackValue;
        }

    }
}
