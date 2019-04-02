package com.tokopedia.transactiondata.entity.response.cartlist.shopgroup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.transactiondata.entity.response.cartlist.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Khoirul on 16/08/18.
 */

public class CartDetail {

    @SerializedName("cart_id")
    @Expose
    private int cartId;
    @SerializedName("errors")
    @Expose
    private List<String> errors = new ArrayList<>();
    @SerializedName("messages")
    @Expose
    private List<String> messages = new ArrayList<>();
    @SerializedName("product")
    @Expose
    private Product product = new Product();

    public int getCartId() {
        return cartId;
    }

    public List<String> getErrors() {
        return errors;
    }

    public List<String> getMessages() {
        return messages;
    }

    public Product getProduct() {
        return product;
    }
}
