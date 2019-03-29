package com.tokopedia.posapp.base.util;

import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;

import com.tokopedia.design.text.watcher.AfterTextWatcher;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author okasurya on 4/13/18.
 */

public class RupiahNumberTextWatcher extends AfterTextWatcher {
    private static final String DEFAULT_VALUE = "0";
    private static final NumberFormat RupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    private static boolean LockTextWatcher = false;

    private EditText editText;
    private String defaultValue;

    public RupiahNumberTextWatcher(EditText editText) {
        this.editText = editText;
        this.defaultValue = DEFAULT_VALUE;
    }

    public RupiahNumberTextWatcher(EditText editText, String defaultValue) {
        this.editText = editText;
        this.defaultValue = defaultValue;
    }

    @Override
    public void afterTextChanged(Editable s) {
        applyFormatter();
        String valueString = removeCurrencyPrefix(s.toString()).replace(",", "");
        if (TextUtils.isEmpty(valueString)) {
            editText.setText(defaultValue);
            editText.setSelection(editText.getText().length());
            return;
        }
    }

    protected void applyFormatter() {
        try {
            int noFirstCharToIgnore = countPrefixCurrency(editText.getText().toString());
            if (editText.length() > noFirstCharToIgnore && !LockTextWatcher) {
                LockTextWatcher = true;
                int tempCursorPos = editText.getSelectionStart();
                int tempLength = editText.length();
                editText.setText(RupiahFormat.format(Long.parseLong(
                        editText.getText().toString()
                                .substring(noFirstCharToIgnore)
                                .replace("Rp", "")
                                .replace(",00", "")
                                .replace(",0", "")
                                .replace(",", "")
                                .replace(".", "")))
                        .replace("Rp", "")
                        .replace(",00", "")
                        .replace(",0", ""));
                // Handler untuk tanda koma
                if (editText.length() - tempLength == 1) // Untuk majuin cursor ketika nambah koma
                {
                    if (editText.length() < (4 + noFirstCharToIgnore)) {
                        tempCursorPos += 1;
                    } else if (editText.getText().charAt(tempCursorPos) != ',') { // Untuk mundur ketika mencoba menghapus koma
                        tempCursorPos += 1;
                    }
                } else if (editText.length() - tempLength == -1) // Mundurin cursor ketika hapus koma
                {
                    tempCursorPos -= 1;
                } else if (editText.length() > (3 + noFirstCharToIgnore)
                        && tempCursorPos < editText.length()
                        && tempCursorPos > noFirstCharToIgnore) {
                    if (editText.getText().charAt(tempCursorPos - 1) == ',') { // Mundurin cursor ketika menambah digit dibelakang koma
                        tempCursorPos -= 1;
                    }
                }

                // Set posisi cursor
                if (tempCursorPos < editText.length() && tempCursorPos > (-1 + noFirstCharToIgnore))
                    editText.setSelection(tempCursorPos);
                else if (tempCursorPos < noFirstCharToIgnore)
                    editText.setSelection(noFirstCharToIgnore);
                else
                    editText.setSelection(editText.length());
                LockTextWatcher = false;
            }
        } catch (NumberFormatException e) {
            LockTextWatcher = false;
            e.printStackTrace();
        }
    }

    private String removeCurrencyPrefix(String string) {
        if (string == null) return null;
        int count = 0;
        for (int i = 0, sizei = string.length(); i < sizei; i++) {
            char charString = string.charAt(i);
            if (Character.isDigit(charString)) {
                break;
            } else {
                count++;
            }
        }
        return string.substring(count);
    }

    private int countPrefixCurrency(String string) {
        int count = 0;
        for (int i = 0, sizei = string.length(); i < sizei; i++) {
            char charString = string.charAt(i);
            if (Character.isDigit(charString)) {
                break;
            } else {
                count++;
            }
        }
        return count;
    }
}
