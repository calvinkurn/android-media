package com.tokopedia.core.router.transactionmodule.sharedata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 27/02/18.
 */

public class CheckPromoCodeCartShipmentRequest {

    @SerializedName("promo_code")
    @Expose
    private String promoCode;
    @SerializedName("data")
    @Expose
    private List<Data> data = new ArrayList<>();

    private CheckPromoCodeCartShipmentRequest(Builder builder) {
        promoCode = builder.promoCode;
        data = builder.data;
    }

    public static class Data {

        @SerializedName("address_id")
        @Expose
        private int addressId;
        @SerializedName("shop_products")
        @Expose
        private List<ShopProduct> shopProducts = new ArrayList<>();

        private Data(Builder builder) {
            addressId = builder.addressId;
            shopProducts = builder.shopProducts;
        }

        public static final class Builder {
            private int addressId;
            private List<ShopProduct> shopProducts;

            public Builder() {
            }

            public Builder addressId(int val) {
                addressId = val;
                return this;
            }

            public Builder shopProducts(List<ShopProduct> val) {
                shopProducts = val;
                return this;
            }

            public Data build() {
                return new Data(this);
            }
        }

    }

    public static class DropshipData {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("telp_no")
        @Expose
        private String telpNo;

        private DropshipData(Builder builder) {
            name = builder.name;
            telpNo = builder.telpNo;
        }

        public static final class Builder {
            private String name;
            private String telpNo;

            public Builder() {
            }

            public Builder name(String val) {
                name = val;
                return this;
            }

            public Builder telpNo(String val) {
                telpNo = val;
                return this;
            }

            public DropshipData build() {
                return new DropshipData(this);
            }
        }
    }


    public static class ProductData {

        @SerializedName("product_id")
        @Expose
        private int productId;
        @SerializedName("product_notes")
        @Expose
        private String productNotes;
        @SerializedName("product_quantity")
        @Expose
        private int productQuantity;

        private ProductData(Builder builder) {
            productId = builder.productId;
            productNotes = builder.productNotes;
            productQuantity = builder.productQuantity;
        }

        public static final class Builder {
            private int productId;
            private String productNotes;
            private int productQuantity;

            public Builder() {
            }

            public Builder productId(int val) {
                productId = val;
                return this;
            }

            public Builder productNotes(String val) {
                productNotes = val;
                return this;
            }

            public Builder productQuantity(int val) {
                productQuantity = val;
                return this;
            }

            public ProductData build() {
                return new ProductData(this);
            }
        }

    }

    public static class ShippingInfo {

        @SerializedName("shipping_id")
        @Expose
        private int shippingId;
        @SerializedName("sp_id")
        @Expose
        private int spId;

        private ShippingInfo(Builder builder) {
            shippingId = builder.shippingId;
            spId = builder.spId;
        }

        public static final class Builder {
            private int shippingId;
            private int spId;

            public Builder() {
            }

            public Builder shippingId(int val) {
                shippingId = val;
                return this;
            }

            public Builder spId(int val) {
                spId = val;
                return this;
            }

            public ShippingInfo build() {
                return new ShippingInfo(this);
            }
        }

    }

    public static class ShopProduct {

        @SerializedName("shop_id")
        @Expose
        private int shopId;
        @SerializedName("is_preorder")
        @Expose
        private int isPreorder;
        @SerializedName("finsurance")
        @Expose
        private int finsurance;
        @SerializedName("shipping_info")
        @Expose
        private ShippingInfo shippingInfo;
        @SerializedName("dropship_data")
        @Expose
        private DropshipData dropshipData;
        @SerializedName("product_data")
        @Expose
        private List<ProductData> productData = new ArrayList<>();
        @SerializedName("fcancel_partial")
        @Expose
        private int fcancelPartial;

        private ShopProduct(Builder builder) {
            shopId = builder.shopId;
            isPreorder = builder.isPreorder;
            finsurance = builder.finsurance;
            shippingInfo = builder.shippingInfo;
            dropshipData = builder.dropshipData;
            productData = builder.productData;
            fcancelPartial = builder.fcancelPartial;
        }

        public static final class Builder {
            private int shopId;
            private int isPreorder;
            private int finsurance;
            private ShippingInfo shippingInfo;
            private DropshipData dropshipData;
            private List<ProductData> productData;
            private int fcancelPartial;

            public Builder() {
            }

            public Builder shopId(int val) {
                shopId = val;
                return this;
            }

            public Builder isPreorder(int val) {
                isPreorder = val;
                return this;
            }

            public Builder finsurance(int val) {
                finsurance = val;
                return this;
            }

            public Builder shippingInfo(ShippingInfo val) {
                shippingInfo = val;
                return this;
            }

            public Builder dropshipData(DropshipData val) {
                dropshipData = val;
                return this;
            }

            public Builder productData(List<ProductData> val) {
                productData = val;
                return this;
            }

            public Builder fcancelPartial(int val) {
                fcancelPartial = val;
                return this;
            }

            public ShopProduct build() {
                return new ShopProduct(this);
            }
        }
    }

    public static final class Builder {
        private String promoCode;
        private List<Data> data;

        public Builder() {
        }

        public Builder promoCode(String val) {
            promoCode = val;
            return this;
        }

        public Builder data(List<Data> val) {
            data = val;
            return this;
        }

        public CheckPromoCodeCartShipmentRequest build() {
            return new CheckPromoCodeCartShipmentRequest(this);
        }
    }
}
