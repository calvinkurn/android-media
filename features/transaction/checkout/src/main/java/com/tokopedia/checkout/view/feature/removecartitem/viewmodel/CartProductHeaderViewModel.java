package com.tokopedia.checkout.view.feature.removecartitem.viewmodel;

/**
 * @author Irfan Khoirul on 24/05/18.
 */
@Deprecated
public class CartProductHeaderViewModel implements CartRemoveProductModel {

    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
