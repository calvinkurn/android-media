package com.tokopedia.purchase_platform.cart.domain.model;

import com.tokopedia.purchase_platform.cart.domain.model.cartlist.CartListData;
import com.tokopedia.purchase_platform.cart.domain.model.cartlist.DeleteCartData;

/**
 * @author anggaprasetiyo on 02/03/18.
 */

public class DeleteAndRefreshCartListData {
    private DeleteCartData deleteCartData;
    private CartListData cartListData;

    public DeleteCartData getDeleteCartData() {
        return deleteCartData;
    }

    public void setDeleteCartData(DeleteCartData deleteCartData) {
        this.deleteCartData = deleteCartData;
    }

    public CartListData getCartListData() {
        return cartListData;
    }

    public void setCartListData(CartListData cartListData) {
        this.cartListData = cartListData;
    }
}
