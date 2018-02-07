package com.tokopedia.design.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.price.DynamicBackgroundSeekBar;
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
