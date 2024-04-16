package com.tokopedia.filter.newdynamicfilter.adapter.viewholder.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.annotation.NonNull;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by henrypriyono on 8/14/17.
 */

public class DecimalRangeInputView extends RangeInputView {

    private NumberTextWatcher minTextWatcher;
    private NumberTextWatcher maxTextWatcher;

    public DecimalRangeInputView(@NonNull Context context) {
        super(context);
        init();
    }

    public DecimalRangeInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DecimalRangeInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        minTextWatcher = new NumberTextWatcher(minValueEditText);
        maxTextWatcher = new NumberTextWatcher(maxValueEditText);
        enableTextWatcher();
    }

    protected void refreshInputText() {
        disableTextWatcher();
        super.refreshInputText();
        enableTextWatcher();
    }

    @Override
    protected void setValueText(int minValue, int maxValue) {
        minValueEditText.setText(formatToRupiah(minValue));
        maxValueEditText.setText(formatToRupiah(maxValue));
        minValueEditText.setSelection(minValueEditText.length());
        maxValueEditText.setSelection(maxValueEditText.length());
    }

    private void disableTextWatcher() {
        minValueEditText.removeTextChangedListener(minTextWatcher);
        maxValueEditText.removeTextChangedListener(maxTextWatcher);
    }

    private void enableTextWatcher() {
        minValueEditText.addTextChangedListener(minTextWatcher);
        maxValueEditText.addTextChangedListener(maxTextWatcher);
    }

    private String formatToRupiah(int value) {
        return NumberFormat.getCurrencyInstance(Locale.US)
                .format(value).replace("$", "").replace(".00","");
    }

}

abstract class AfterTextWatcher implements TextWatcher {
    @Override
    public final void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public final void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public abstract void afterTextChanged(Editable s);
}

class NumberTextWatcher extends AfterTextWatcher {

    private static final String DEFAULT_VALUE = "0";
    private static final NumberFormat RupiahFormat = NumberFormat.getCurrencyInstance(Locale.US);
    private static boolean LockTextWatcher = false;
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

    private int countPrefixCurrency(String string) {
        int count = 0;
        for (int i=0, sizei = string.length();i<sizei ; i++) {
            char charString = string.charAt(i);
            if (Character.isDigit(charString)){
                break;
            }
            else {
                count++;
            }
        }
        return count;
    }

    private void setToRupiahCheckPrefix(EditText et){
        try {
            int noFirstCharToIgnore = countPrefixCurrency(et.getText().toString());
            if(et.length()>noFirstCharToIgnore && !LockTextWatcher){
                LockTextWatcher = true;
                int tempCursorPos = et.getSelectionStart();
                int tempLength = et.length();
                et.setText(RupiahFormat.format(Long.parseLong(
                                et.getText().toString()
                                        .substring(noFirstCharToIgnore)
                                        .replace("$", "")
                                        .replace(".00", "")
                                        .replace(".0", "")
                                        .replace(",", "")
                                        .replace(".", "")))
                        .replace("$", "")
                        .replace(".00", "")
                        .replace(".0", ""));
                // Handler untuk tanda koma
                if(et.length() - tempLength == 1) // Untuk majuin cursor ketika nambah koma
                {
                    if(et.length()< (4 + noFirstCharToIgnore)) {
                        tempCursorPos += 1;
                    }
                    else if(et.getText().charAt(tempCursorPos) != '.') { // Untuk mundur ketika mencoba menghapus koma
                        tempCursorPos += 1;
                    }
                }
                else if(et.length() - tempLength == -1) // Mundurin cursor ketika hapus koma
                {
                    tempCursorPos -= 1;
                }
                else if(et.length()>(3 + noFirstCharToIgnore)
                        && tempCursorPos < et.length()
                        && tempCursorPos > noFirstCharToIgnore) {
                    if (et.getText().charAt(tempCursorPos - 1) == '.') { // Mundurin cursor ketika menambah digit dibelakang koma
                        tempCursorPos -= 1;
                    }
                }

                // Set posisi cursor
                if(tempCursorPos < et.length() && tempCursorPos > (-1+noFirstCharToIgnore) )
                    et.setSelection(tempCursorPos);
                else if(tempCursorPos< noFirstCharToIgnore )
                    et.setSelection(noFirstCharToIgnore);
                else
                    et.setSelection(et.length());
                LockTextWatcher = false;
            }
        } catch (NumberFormatException e) {
            LockTextWatcher = false;
            e.printStackTrace();
        }
    }

    protected void applyFormatter() {
        setToRupiahCheckPrefix(editText);
    }

    @Override
    public void afterTextChanged(Editable s) {
        applyFormatter();
        String valueString = removeCurrencyPrefix(s.toString());
        valueString = StringUtils.removeComma(valueString);
        if (TextUtils.isEmpty(valueString)) {
            editText.setText(defaultValue);
            editText.setSelection(editText.getText().length());
            return;
        }
        double value;
        try {
            value = Double.parseDouble(valueString);
        } catch (NumberFormatException e) {
            value = 0d;
        }
        onNumberChanged(value);
    }

    public void onNumberChanged(double number) {

    }

    public static String removeCurrencyPrefix(String string){
        if (string == null) return null;
        int count = 0;
        for (int i=0, sizei = string.length();i<sizei ; i++) {
            char charString = string.charAt(i);
            if (Character.isDigit(charString)){
                break;
            }
            else {
                count++;
            }
        }
        return string.substring(count);
    }
}
