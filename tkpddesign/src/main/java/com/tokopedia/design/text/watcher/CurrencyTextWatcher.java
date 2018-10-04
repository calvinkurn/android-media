package com.tokopedia.design.text.watcher;

import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;

import com.tokopedia.design.intdef.CurrencyEnum;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.design.utils.StringUtils;

/**
 * Improvement from NumberTextWatcher, we can define what currency format we have
 * can be Rp 123 or 213 IDR or $123 or 213USD, etc
 */
public class CurrencyTextWatcher extends AfterTextWatcher {

    private String defaultValue;
    private EditText editText;
    private boolean useCommaForThousand;

    public String format;
    public int prefixLength = 0;

    public CurrencyTextWatcher(EditText editText) {
        this(editText, null, true);
    }

    public CurrencyTextWatcher(EditText editText, CurrencyEnum currencyEnum) {
        this(editText, currencyEnum.getFormat(), currencyEnum.isUseCommaAsThousand());
    }

    public CurrencyTextWatcher(EditText editText, String format, boolean useCommaForThousand) {
        this.editText = editText;
        this.defaultValue = "0";
        this.useCommaForThousand = useCommaForThousand;
        setFormat(format);
    }

    private OnNumberChangeListener onNumberChangeListener;

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public interface OnNumberChangeListener {
        void onNumberChanged(double value);
    }

    public void setOnNumberChangeListener(OnNumberChangeListener onNumberChangeListener) {
        this.onNumberChangeListener = onNumberChangeListener;
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

        double doubleValue = StringUtils.convertToNumeric(sString, useCommaForThousand);
        if (onNumberChangeListener!= null) {
            onNumberChangeListener.onNumberChanged(doubleValue);
        }
        editText.removeTextChangedListener(this);
        if (doubleValue == 0) {
            editText.setText(String.format(format, defaultValue));
            editText.setSelection(editText.getText().length());
        } else {
            int selectionStart = editText.getSelectionStart() - prefixLength;
            if (selectionStart < 0) {
                selectionStart = 0;
            }
            CurrencyFormatUtil.ThousandString thousandString =
                    CurrencyFormatUtil.getThousandSeparatorString(
                            doubleValue, useCommaForThousand, selectionStart);
            editText.setText(String.format(format, thousandString.getFormattedString()));
            editText.setSelection(Math.min(editText.length(), thousandString.getSelection() + prefixLength));
        }
        editText.addTextChangedListener(this);
    }

}
