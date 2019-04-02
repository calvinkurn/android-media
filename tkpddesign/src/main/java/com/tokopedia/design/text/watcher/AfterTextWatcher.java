package com.tokopedia.design.text.watcher;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by User on 10/23/2017.
 */

public abstract class AfterTextWatcher implements TextWatcher {
    @Override
    public final void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public final void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public abstract void afterTextChanged(Editable s);
}
