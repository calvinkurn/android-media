package com.tokopedia.posapp.payment.cardscanner.view.widget;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by okasurya on 8/16/17.
 */

public class CreditCardTextWatcher implements TextWatcher {
    private boolean lock = false;

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (!lock) {
            lock = true;
            for (int i = 4; i < editable.length(); i += 5) {
                if (editable.toString().charAt(i) != ' ') {
                    editable.insert(i, " ");
                }
            }
            lock = false;
        }
    }
}
