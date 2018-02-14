package com.tokopedia.core.router.transactionmodule.sharedata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 13/02/18.
 */

public class AddToCartRequest {

    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("shop_id")
    @Expose
    private int shopId;

    private AddToCartRequest(Builder builder) {
        setProductId(builder.productId);
        setQuantity(builder.quantity);
        setNotes(builder.notes);
        setShopId(builder.shopId);
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }


    public static final class Builder {
        private int productId;
        private int quantity;
        private String notes;
        private int shopId;

        public Builder() {
        }

        public Builder productId(int val) {
            productId = val;
            return this;
        }

        public Builder quantity(int val) {
            quantity = val;
            return this;
        }

        public Builder notes(String val) {
            notes = val;
            return this;
        }

        public Builder shopId(int val) {
            shopId = val;
            return this;
        }

        public AddToCartRequest build() {
            return new AddToCartRequest(this);
        }
    }
}
