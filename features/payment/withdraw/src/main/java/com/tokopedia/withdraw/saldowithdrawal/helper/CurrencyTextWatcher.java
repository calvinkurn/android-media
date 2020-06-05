package com.tokopedia.withdraw.saldowithdrawal.helper;

import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;

import com.tokopedia.utils.text.currency.AfterTextWatcher;
import com.tokopedia.utils.text.currency.CurrencyFormatHelper;
import com.tokopedia.utils.text.currency.StringUtils;

/**
 * Improvement from NumberTextWatcher, we can define what currency format we have
 * can be Rp 123 or 213 IDR or $123 or 213USD, etc
 */
public class CurrencyTextWatcher extends AfterTextWatcher {

    public static final int DEFAULT_MAX_LENGTH = 14;

    private String defaultValue;
    private int maxLength = DEFAULT_MAX_LENGTH;
    private EditText editText;
    private boolean useCommaInThousandSeparator;

    public String format;
    public int prefixLength = 0;

    public CurrencyTextWatcher(EditText editText, boolean useCommaInThousandSeparator) {
        this.editText = editText;
        this.useCommaInThousandSeparator = useCommaInThousandSeparator;
        this.defaultValue = "0";
        setFormat("Rp %s");
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setFormat(String format) {
        if (TextUtils.isEmpty(format)) {
            this.format = "%s";
        } else {
            this.format = format;
        }
        this.prefixLength = this.format.indexOf("%");
        if (prefixLength < 0) {
            prefixLength = 0;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        String sString = s.toString();
        if (sString.length() >= maxLength + prefixLength) {
            sString = sString.substring(0, maxLength + prefixLength);
        }
        double doubleValue = StringUtils.INSTANCE.convertToNumeric(sString, useCommaInThousandSeparator);
        editText.removeTextChangedListener(this);
        if (doubleValue == 0) {
            editText.setText(CurrencyFormatHelper.INSTANCE.convertToRupiah(String.valueOf(doubleValue)));
            editText.setSelection(editText.getText().length());
        } else {
            editText.setText(CurrencyFormatHelper.INSTANCE.convertToRupiah(sString));
            editText.setSelection(editText.length());
        }
        editText.addTextChangedListener(this);
    }

}
