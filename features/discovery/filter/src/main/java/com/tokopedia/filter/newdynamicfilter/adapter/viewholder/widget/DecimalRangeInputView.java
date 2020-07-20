package com.tokopedia.filter.newdynamicfilter.adapter.viewholder.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.tokopedia.design.text.RangeInputView;
import com.tokopedia.design.text.watcher.NumberTextWatcher;

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
