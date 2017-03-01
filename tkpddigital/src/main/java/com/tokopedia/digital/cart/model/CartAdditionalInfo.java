package com.tokopedia.digital.cart.model;

import java.util.List;

/**
 * Created by Nabilla Sabbaha on 3/1/2017.
 */

public class CartAdditionalInfo {

    private String title;

    private List<CartItemDigital> cartItemDigitalList;

    public CartAdditionalInfo(String title, List<CartItemDigital> cartItemDigitalList) {
        this.title = title;
        this.cartItemDigitalList = cartItemDigitalList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CartItemDigital> getCartItemDigitalList() {
        return cartItemDigitalList;
    }

    public void setCartItemDigitalList(List<CartItemDigital> cartItemDigitalList) {
        this.cartItemDigitalList = cartItemDigitalList;
    }
}
