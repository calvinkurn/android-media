package com.tokopedia.checkout.view.view.cartlist.removecartitem.viewmodel;

/**
 * @author Irfan Khoirul on 24/05/18.
 */

public class CartProductHeaderViewModel implements CartRemoveProductModel {

    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
