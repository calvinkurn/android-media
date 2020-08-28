package com.tokopedia.topads.common.util;

import android.widget.EditText;

import com.tokopedia.design.text.watcher.NumberTextWatcher;

/**
 * Created by Nathaniel on 3/3/2017.
 */

public class CurrencyIdrTextWatcher extends NumberTextWatcher {

    public CurrencyIdrTextWatcher(EditText editText) {
        super(editText);
    }

    public CurrencyIdrTextWatcher(EditText editText, String defaultValue) {
        super(editText, defaultValue);
    }
}
