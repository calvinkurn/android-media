package com.tokopedia.purchase_platform.common.utils;

import android.text.Editable;

/**
 * Created by Irfan Khoirul on 18/07/18.
 */

public class QuantityWrapper {
    private int qtyBefore;
    private Editable editable;

    public int getQtyBefore() {
        return qtyBefore;
    }

    public void setQtyBefore(int qtyBefore) {
        this.qtyBefore = qtyBefore;
    }

    public Editable getEditable() {
        return editable;
    }

    public void setEditable(Editable editable) {
        this.editable = editable;
    }
}
