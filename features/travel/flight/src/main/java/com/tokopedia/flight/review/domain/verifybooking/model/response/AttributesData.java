
package com.tokopedia.flight.review.domain.verifybooking.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttributesData {

    @SerializedName("cart_items")
    @Expose
    private List<CartItem> cartItems;
    @SerializedName("promo")
    @Expose
    private Promo promo;

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Promo getPromo() {
        return promo;
    }

    public void setPromo(Promo promo) {
        this.promo = promo;
    }

}
