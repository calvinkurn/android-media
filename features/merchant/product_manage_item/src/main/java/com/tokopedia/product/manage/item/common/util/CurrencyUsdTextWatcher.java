package com.tokopedia.product.manage.item.common.util;

import android.widget.EditText;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.design.text.watcher.NumberTextWatcher;

/**
 * Created by Nathaniel on 3/3/2017.
 */

public class CurrencyUsdTextWatcher extends NumberTextWatcher {

    public CurrencyUsdTextWatcher(EditText editText) {
        super(editText);
    }

    public CurrencyUsdTextWatcher(EditText editText, String defaultValue) {
        super(editText, defaultValue);
    }

    @Override
    public void applyFormatter() {
        CurrencyFormatHelper.SetToDollar(editText);
    }
}
