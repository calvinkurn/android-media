package com.tokopedia.purchase_platform.common.utils;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Irfan Khoirul on 18/07/18.
 */

public class QuantityTextWatcher implements TextWatcher {

    public static final int TEXTWATCHER_QUANTITY_DEBOUNCE_TIME = 1000;

    private int qtyBefore;
    private QuantityWrapper quantityWrapper = new QuantityWrapper();
    private QuantityTextwatcherListener quantityTextwatcherListener;

    public QuantityTextWatcher(QuantityTextwatcherListener quantityTextwatcherListener) {
        this.quantityTextwatcherListener = quantityTextwatcherListener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        try {
            qtyBefore = Integer.parseInt(s.toString());
        } catch (NumberFormatException e) {
            qtyBefore = 0;
            e.printStackTrace();
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        quantityWrapper.setEditable(editable);
        quantityWrapper.setQtyBefore(qtyBefore);
        if (quantityTextwatcherListener != null) {
            quantityTextwatcherListener.onQuantityChanged(quantityWrapper);
        }
    }

    public interface QuantityTextwatcherListener {
        void onQuantityChanged(QuantityWrapper quantityWrapper);
    }

}
