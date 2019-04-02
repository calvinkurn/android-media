package com.tokopedia.design.text.watcher;

import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;

import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.design.utils.StringUtils;

/**
 * Created by Nathaniel on 3/3/2017.
 */

public class NumberTextWatcher extends AfterTextWatcher {

    private static final String DEFAULT_VALUE = "0";

    protected EditText editText;

    private String defaultValue;

    public NumberTextWatcher(EditText editText) {
        this.editText = editText;
        this.defaultValue = DEFAULT_VALUE;
    }

    public NumberTextWatcher(EditText editText, String defaultValue) {
        this.editText = editText;
        this.defaultValue = defaultValue;
    }

    protected void applyFormatter() {
        CurrencyFormatHelper.setToRupiahCheckPrefix(editText);
    }

    @Override
    public void afterTextChanged(Editable s) {
        applyFormatter();
        String valueString = CurrencyFormatHelper.removeCurrencyPrefix(s.toString());
        valueString = StringUtils.removeComma(valueString);
        if (TextUtils.isEmpty(valueString)) {
            editText.setText(defaultValue);
            editText.setSelection(editText.getText().length());
            return;
        }
        double value = Double.parseDouble(valueString);
        onNumberChanged(value);
    }

    public void onNumberChanged(double number) {

    }
}
