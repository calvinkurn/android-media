package com.tokopedia.design.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * improvement from CurrencyFormatHelper
 * We can format the text without specifying the prefix (Rp/IDR/USD/etc/etc)
 *
 * This Util will return the ThousandString object, instead directly modified the edittext directly
 * to set the EditText, use the code like
 * CurrencyFormatUtil.ThousandString thousandString = CurrencyFormatUtil.getThousandSeparatorString(nonNumericString, false, editText.getSelectionStart());
 * editText.setText(thousandString.getFormattedString());
 * editText.setSelection(Math.min(editText.length(), thousandString.getSelection()));
 */
public class CurrencyFormatUtil {
    private static final NumberFormat dotFormat = NumberFormat.getNumberInstance(new Locale("in", "id"));
    private static final NumberFormat commaFormat = NumberFormat.getNumberInstance(new Locale("en", "US"));

    public static class ThousandString {
        private String formattedString;
        private int selection;

        public ThousandString(String formattedString, int selection) {
            this.formattedString = formattedString;
            this.selection = selection;
        }

        public String getFormattedString() {
            return formattedString;
        }

        public int getSelection() {
            return selection;
        }

        public void setFormattedString(String formattedString) {
            this.formattedString = formattedString;
        }

        public void setSelection(int selection) {
            this.selection = selection;
        }
    }

    /**
     * @param valueToFormat eg.14123.5
     * @param useComma e/g true
     * @param selectionStart eg. 0
     * @return 14,123.5 with selection start 0
     */
    public static ThousandString getThousandSeparatorString(double valueToFormat, boolean useComma, int selectionStart) {
        String textToFormat = String.valueOf(valueToFormat);
        String formattedString = textToFormat;
        int cursorEnd = selectionStart;
        char separatorString = ',';

        int sourceLength = textToFormat.length();
        try {
            if (sourceLength > 0) {
                if (useComma) {
                    formattedString = commaFormat.format(valueToFormat);
                    separatorString = ',';
                } else {
                    formattedString = dotFormat.format(valueToFormat);
                    separatorString = '.';
                }
                // same with before, just return as is.
                int resultLength = formattedString.length();
                if (textToFormat.equals(formattedString)) {
                    return new ThousandString(textToFormat, resultLength);
                }

                int tempCursorPos = selectionStart;

                // Handler untuk tanda koma
                if (resultLength - selectionStart == 1) {
                    // Untuk majuin cursor ketika nambah koma
                    if (formattedString.length() < 4) {
                        tempCursorPos += 1;
                    } else if (formattedString.charAt(tempCursorPos) != separatorString) {
                        // Untuk mundur ketika mencoba menghapus koma
                        tempCursorPos += 1;
                    }
                } else if (resultLength - selectionStart == -1) {
                    // Mundurin cursor ketika hapus koma
                    tempCursorPos -= 1;
                } else if (resultLength > 3 && selectionStart < resultLength && selectionStart > 0) {
                    if (formattedString.charAt(selectionStart - 1) == separatorString) {
                        // Mundurin cursor ketika menambah digit dibelakang koma
                        tempCursorPos -= 1;
                    }
                } else {
                    tempCursorPos = resultLength;
                }

                // Set posisi cursor
                if (tempCursorPos < resultLength && tempCursorPos > -1) {
                    cursorEnd = tempCursorPos;
                } else if (tempCursorPos < 0) {
                    cursorEnd = 0;
                } else {
                    cursorEnd = resultLength;
                }
                return new ThousandString(formattedString, cursorEnd);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ThousandString(textToFormat, selectionStart);
        }
        return new ThousandString(textToFormat, selectionStart);
    }

    public static String convertPriceValueToIdrFormat(int price, boolean hasSpace) {
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        kursIndonesia.setMaximumFractionDigits(0);
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp" + (hasSpace?" ":""));
        formatRp.setGroupingSeparator('.');
        formatRp.setMonetaryDecimalSeparator('.');
        formatRp.setDecimalSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        String result = kursIndonesia.format(price);

        return result.replace(",", ".");
    }

    public static String convertPriceValueToIdrFormatNoSpace(int price) {
        return convertPriceValueToIdrFormat(price, false);
    }
}
