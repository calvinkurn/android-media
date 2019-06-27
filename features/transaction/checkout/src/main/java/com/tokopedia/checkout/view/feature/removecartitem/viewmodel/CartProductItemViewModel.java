package com.tokopedia.checkout.view.feature.removecartitem.viewmodel;

import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;

/**
 * @author Irfan Khoirul on 24/05/18.
 */
@Deprecated
public class CartProductItemViewModel implements CartRemoveProductModel {

    private CartItemData cartItemData;
    private boolean checked;

    public CartItemData getCartItemData() {
        return cartItemData;
    }

    public void setCartItemData(CartItemData cartItemData) {
        this.cartItemData = cartItemData;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
