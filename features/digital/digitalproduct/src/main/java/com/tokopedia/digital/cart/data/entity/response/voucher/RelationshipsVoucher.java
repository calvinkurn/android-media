package com.tokopedia.digital.cart.data.entity.response.voucher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 3/7/17.
 */

public class RelationshipsVoucher {

    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("cart")
    @Expose
    private Cart cart;

    public Category getCategory() {
        return category;
    }

    public Cart getCart() {
        return cart;
    }
}
