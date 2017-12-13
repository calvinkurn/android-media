package com.tokopedia.tkpd.thankyou.data.pojo.digital;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by okasurya on 12/8/17.
 */

public class DigitalTrackerData {
    @SerializedName("event")
    @Expose
    private String event;
    @SerializedName("cd1")
    @Expose
    private String cd1;
    @SerializedName("cd2")
    @Expose
    private String cd2;
    @SerializedName("cd3")
    @Expose
    private String cd3;
    @SerializedName("cd4")
    @Expose
    private String cd4;
    @SerializedName("cm1")
    @Expose
    private String cm1;
    @SerializedName("ecommerce")
    @Expose
    private Ecommerce ecommerce;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getCd1() {
        return cd1;
    }

    public void setCd1(String cd1) {
        this.cd1 = cd1;
    }

    public String getCd2() {
        return cd2;
    }

    public void setCd2(String cd2) {
        this.cd2 = cd2;
    }

    public String getCd3() {
        return cd3;
    }

    public void setCd3(String cd3) {
        this.cd3 = cd3;
    }

    public String getCd4() {
        return cd4;
    }

    public void setCd4(String cd4) {
        this.cd4 = cd4;
    }

    public String getCm1() {
        return cm1;
    }

    public void setCm1(String cm1) {
        this.cm1 = cm1;
    }

    public Ecommerce getEcommerce() {
        return ecommerce;
    }

    public void setEcommerce(Ecommerce ecommerce) {
        this.ecommerce = ecommerce;
    }

    public static class Ecommerce {
        @SerializedName("purchase")
        @Expose
        private Purchase purchase;

        public Purchase getPurchase() {
            return purchase;
        }

        public void setPurchase(Purchase purchase) {
            this.purchase = purchase;
        }
    }

    public static class Purchase {
        @SerializedName("actionField")
        @Expose
        private ActionField actionField;
        @SerializedName("products")
        @Expose
        private List<Product> products;

        public ActionField getActionField() {
            return actionField;
        }

        public void setActionField(ActionField actionField) {
            this.actionField = actionField;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }
    }

    public static class ActionField {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("affiliation")
        @Expose
        private String affiliation;
        @SerializedName("revenue")
        @Expose
        private String revenue;
        @SerializedName("tax")
        @Expose
        private String tax;
        @SerializedName("shipping")
        @Expose
        private String shipping;
        @SerializedName("coupon")
        @Expose
        private String coupon;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAffiliation() {
            return affiliation;
        }

        public void setAffiliation(String affiliation) {
            this.affiliation = affiliation;
        }

        public String getRevenue() {
            return revenue;
        }

        public void setRevenue(String revenue) {
            this.revenue = revenue;
        }

        public String getTax() {
            return tax;
        }

        public void setTax(String tax) {
            this.tax = tax;
        }

        public String getShipping() {
            return shipping;
        }

        public void setShipping(String shipping) {
            this.shipping = shipping;
        }

        public String getCoupon() {
            return coupon;
        }

        public void setCoupon(String coupon) {
            this.coupon = coupon;
        }
    }

    public static class Product {
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("brand")
        @Expose
        private String brand;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("variant")
        @Expose
        private String variant;
        @SerializedName("quantity")
        @Expose
        private String quantity;
        @SerializedName("coupon")
        @Expose
        private String coupon;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

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

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getVariant() {
            return variant;
        }

        public void setVariant(String variant) {
            this.variant = variant;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getCoupon() {
            return coupon;
        }

        public void setCoupon(String coupon) {
            this.coupon = coupon;
        }
    }
}
