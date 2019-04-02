package com.tokopedia.product.manage.item.common.util;

import android.text.Editable;
import android.widget.EditText;

import com.tokopedia.design.text.watcher.NumberTextWatcher;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by yoshua on 08/06/18.
 */

public class CurrencyUsdPrefixEdittextTextWatcher extends NumberTextWatcher {

    private static final int DECIMAL_DIGITS = 2;
    private String currentValue = "0.00";
    private DecimalFormat decimalFormat;
    public final static String PREFIX_RUPIAH = "Rp ";
    public final static String PREFIX_DOLLAR = "$US ";

    public CurrencyUsdPrefixEdittextTextWatcher(EditText editText) {
        super(editText);
        decimalFormat =  (DecimalFormat) DecimalFormat.getCurrencyInstance(Locale.US);
    }

    public CurrencyUsdPrefixEdittextTextWatcher(EditText editText, String defaultValue) {
        super(editText, defaultValue);
        decimalFormat =  (DecimalFormat) DecimalFormat.getCurrencyInstance(Locale.US);
    }

    @Override
    public void afterTextChanged(Editable s) {
        editText.removeTextChangedListener(this);
        formatToDollar(s.toString());
        editText.addTextChangedListener(this);
    }


    public void formatToDollar(String text){
        int tempCursorPos = editText.getSelectionStart();
        int tempLength = editText.length();

        if(text.equalsIgnoreCase(PREFIX_DOLLAR)){
            setTextEditText(currentValue);
            setCursorPosition(tempCursorPos, tempLength);
        } else {
            text = text.replaceAll("[^\\d]", "");

            if (text.length() <= DECIMAL_DIGITS){
                String formatString = "%" + DECIMAL_DIGITS + "s";
                text = String.format(formatString, text).replace(' ', '0');
            }

            String decimalString = new StringBuilder(text).insert(text.length() - DECIMAL_DIGITS, '.').toString();
            double decimalValue = Double.valueOf(decimalString);

            text = convertToDollarWithCurrency(decimalValue);
            setTextEditText(text);
            setCursorPosition(tempCursorPos, tempLength);

            onNumberChanged(decimalValue);
        }
    }

    public String convertToDollarWithCurrency(Double decimalValue){
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormat().getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        decimalFormat.setMinimumFractionDigits(DECIMAL_DIGITS);
        return decimalFormat.format(decimalValue);
    }

    public void setTextEditText(String text){
        editText.setText(text);
        currentValue = text;
    }

    public void setCursorPosition(int tempCursorPos, int tempLength){
        if(editText.length() - tempLength == 1)
        {
           if(tempCursorPos>-1)
                if(editText.getText().charAt(tempCursorPos) != ',')
                    tempCursorPos += 1;

        }  else if(editText.length() - tempLength == -1)
        {
            tempCursorPos -= 1;
        }

        if(tempCursorPos < editText.length()&&tempCursorPos>-1)
            editText.setSelection(tempCursorPos);
        else if(tempCursorPos<0)
            editText.setSelection(0);
        else
            editText.setSelection(editText.length());
    }
}
