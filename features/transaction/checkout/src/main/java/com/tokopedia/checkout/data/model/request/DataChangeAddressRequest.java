package com.tokopedia.checkout.data.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public class DataChangeAddressRequest {

    @SerializedName("cart_id_str")
    @Expose
    private String cartIdStr;

    @SerializedName("product_id")
    @Expose
    private int productId;

    @SerializedName("address_id")
    @Expose
    private int addressId;

    @SerializedName("note")
    @Expose
    private String notes;

    @SerializedName("qty")
    @Expose
    private int quantity;

    public void setCartIdStr(String cartIdStr) {
        this.cartIdStr = cartIdStr;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
