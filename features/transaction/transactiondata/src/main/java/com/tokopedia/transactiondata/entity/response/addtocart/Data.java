package com.tokopedia.transactiondata.entity.response.addtocart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 26/06/18.
 */
public class Data {

    @SerializedName("cart_id")
    @Expose
    private long cartId;
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
    @SerializedName("customer_id")
    @Expose
    private int customerId;

    public long getCartId() {
        return cartId;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getNotes() {
        return notes;
    }

    public int getShopId() {
        return shopId;
    }

    public int getCustomerId() {
        return customerId;
    }
}
