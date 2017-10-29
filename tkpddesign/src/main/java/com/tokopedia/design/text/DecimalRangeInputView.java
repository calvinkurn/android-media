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

public class DecimalRangeInputView extends BaseCustomView {

    private static final int VALUE_STOP_COUNT = 100;
    private static final double DEFAULT_POWER = 2;

    private TextView minLabelTextView;
    private TextView maxLabelTextView;
    private EditText minValueEditText;
    private EditText maxValueEditText;
    private DynamicBackgroundSeekBar dynamicBackgroundSeekBar;
    private View minButton;
    private View maxButton;

    private OnValueChangedListener onValueChangedListener;
    private NumberTextWatcher minTextWatcher;
    private NumberTextWatcher maxTextWatcher;

    private int seekBarButtonSize;
    private int minBound = 0;
    private int maxBound = 0;
    private int minValue = 0;
    private int maxValue = 0;
    private float valueRange = 0;
    private float seekBarWidth = 0;
    private float seekBarRange = 0;
    private float seekBarLeftOffset = 0;
    private int valueList[] = new int[VALUE_STOP_COUNT];
    private double baseRange = 0;
    private double power = DEFAULT_POWER;

    private boolean isMinButtonDragging = false;
    private boolean isMaxButtonDragging = false;

    private String minLabelText;
    private String maxLabelText;

    public DecimalRangeInputView(@NonNull Context context) {
        super(context);
        init();
    }

    public DecimalRangeInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DecimalRangeInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.DecimalRangeInputView);
        try {
            minLabelText = styledAttributes.getString(R.styleable.DecimalRangeInputView_driv_label_min);
            maxLabelText = styledAttributes.getString(R.styleable.DecimalRangeInputView_driv_label_max);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void init() {
        final View view = inflate(getContext(), R.layout.widget_decimal_range_input_view, this);
        seekBarButtonSize = getResources().getDimensionPixelSize(R.dimen.price_seekbar_button_size);
        view.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        seekBarLeftOffset = dynamicBackgroundSeekBar.getX();
                        seekBarWidth = dynamicBackgroundSeekBar.getWidth();
                        seekBarRange = seekBarWidth - 2 * seekBarButtonSize;
                        refreshButtonPosition();
                    }
                });
        dynamicBackgroundSeekBar = (DynamicBackgroundSeekBar) view.findViewById(R.id.seekbar_background);
        minLabelTextView = (TextView) view.findViewById(R.id.min_label);
        maxLabelTextView = (TextView) view.findViewById(R.id.max_label);
        minValueEditText = (EditText) view.findViewById(R.id.min_value);
        maxValueEditText = (EditText) view.findViewById(R.id.max_value);
        minButton = view.findViewById(R.id.min_button);
        maxButton = view.findViewById(R.id.max_button);

        minTextWatcher = new NumberTextWatcher(minValueEditText);
        maxTextWatcher = new NumberTextWatcher(maxValueEditText);

        minValueEditText.addTextChangedListener(minTextWatcher);
        maxValueEditText.addTextChangedListener(maxTextWatcher);
        minValueEditText.setOnFocusChangeListener(new MinInputListener());
        maxValueEditText.setOnFocusChangeListener(new MaxInputListener());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!TextUtils.isEmpty(minLabelText)) {
            minLabelTextView.setText(minLabelText);
        }
        if (!TextUtils.isEmpty(maxLabelText)) {
            maxLabelTextView.setText(maxLabelText);
        }
        invalidate();
        requestLayout();
    }

    public void setData(String minText, String maxText,
                        int minBound, int maxBound, int minValue, int maxValue) {
        setData(minText, maxText, minBound, maxBound, minValue, maxValue, 0);
    }

    public void setData(String minText, String maxText,
                        int minBound, int maxBound, int minValue, int maxValue, double power) {
        setLabel(minText, maxText);
        setPower(power);
        setData(minBound, maxBound, minValue, maxValue);
    }

    private void setLabel(String minText, String maxText) {
        minLabelTextView.setText(minText);
        maxLabelTextView.setText(maxText);
    }

    private void setPower(double power) {
        if (power > 0) {
            this.power = power;
        } else {
            this.power = DEFAULT_POWER;
        }
    }

    public void setData(int minBound, int maxBound, int minValue, int maxValue) {
        this.minBound = minBound;
        this.maxBound = maxBound;
        this.minValue = minValue;
        this.maxValue = maxValue;

        updateValueCalculation();
        refreshButtonPosition();
        refreshInputText();
    }

    private void updateValueCalculation() {
        valueRange = maxBound - minBound;
        baseRange = Math.pow(valueRange, 1 / power);

        valueList[0] = minBound;
        valueList[VALUE_STOP_COUNT - 1] = maxBound;

        for (int i = 1; i < VALUE_STOP_COUNT - 1; i++) {
            double base = (double) i / (VALUE_STOP_COUNT - 1) * baseRange;
            valueList[i] = minBound + (int) Math.pow(base, power);
            int delta = valueList[i] - valueList[i - 1];
            if (delta > 0) {
                int truncatedValue = valueList[i] % (int) Math.pow(10, (int) Math.log10(delta));
                valueList[i] -= truncatedValue;
            }
        }
    }

    private void refreshButtonPosition() {
        minButton.setX(getMinButtonX());
        maxButton.setX(getMaxButtonX());
        refreshSeekbarBackground();
    }

    private int getMinButtonX() {
        return getPositionFromValue(minValue);
    }

    private int getMaxButtonX() {
        return getPositionFromValue(maxValue) + seekBarButtonSize;
    }

    private int getPositionFromValue(int value) {
        double base = Math.pow(value - minBound, 1 / power);
        double result = base / baseRange * seekBarRange;
        return (int) (result + seekBarLeftOffset);
    }

    private void refreshSeekbarBackground() {
        dynamicBackgroundSeekBar
                .setFirstPointPercentage((minButton.getX() - seekBarLeftOffset) / seekBarWidth);
        dynamicBackgroundSeekBar
                .setSecondPointPercentage((maxButton.getX() - seekBarLeftOffset) / seekBarWidth);
        dynamicBackgroundSeekBar.invalidate();
    }

    private void refreshInputText() {
        disableTextWatcher();
        minValueEditText.setText(formatToRupiah(minValue));
        maxValueEditText.setText(formatToRupiah(maxValue));
        minValueEditText.setSelection(minValueEditText.length());
        maxValueEditText.setSelection(maxValueEditText.length());
        if (onValueChangedListener != null) {
            onValueChangedListener.onValueChanged(minValue, maxValue);
        }
        enableTextWatcher();
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

    private int getMinValue() {
        int minValue = getValueFromPosition(minButton.getX());
        if (minValue < maxValue) {
            return minValue;
        } else {
            return maxValue;
        }
    }

    private int getMaxValue() {
        int maxValue = getValueFromPosition(maxButton.getX() - seekBarButtonSize);
        if (maxValue > minValue) {
            return maxValue;
        } else {
            return minValue;
        }
    }

    private int getValueFromPosition(float x) {
        float valueStopPosition = (x - seekBarLeftOffset) / seekBarRange * (VALUE_STOP_COUNT - 1);
        return valueList[Math.round(valueStopPosition)];
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isPointInsideView(event.getRawX(), event.getRawY(), minButton)) {
                    isMinButtonDragging = true;
                    isMaxButtonDragging = false;
                    minButton.setBackgroundResource(R.drawable.price_input_seekbar_button_pressed);
                } else if (isPointInsideView(event.getRawX(), event.getRawY(), maxButton)) {
                    isMaxButtonDragging = true;
                    isMinButtonDragging = false;
                    maxButton.setBackgroundResource(R.drawable.price_input_seekbar_button_pressed);
                }
                break;
            case MotionEvent.ACTION_UP:
                isMinButtonDragging = false;
                isMaxButtonDragging = false;
                minButton.setBackgroundResource(R.drawable.price_input_seekbar_button_neutral);
                maxButton.setBackgroundResource(R.drawable.price_input_seekbar_button_neutral);
                break;
            case MotionEvent.ACTION_MOVE:
                float targetX = event.getRawX() - seekBarButtonSize;
                if (isMinButtonDragging) {
                    float position = normalizeMinButtonPosition(targetX);
                    minButton.setX(position);
                    minValue = getMinValue();
                    refreshSeekbarBackground();
                    refreshInputText();
                    requestDisallowInterceptTouchEvent(true);
                } else if (isMaxButtonDragging) {
                    float position = normalizeMaxButtonPosition(targetX);
                    maxButton.setX(position);
                    maxValue = getMaxValue();
                    refreshSeekbarBackground();
                    refreshInputText();
                    requestDisallowInterceptTouchEvent(true);
                } else {
                    requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        return true;
    }

    private float normalizeMinButtonPosition(float x) {
        if (x < 0) {
            return 0;
        } else if (x > maxButton.getX() - seekBarButtonSize) {
            return maxButton.getX() - seekBarButtonSize;
        } else {
            return x;
        }
    }

    private float normalizeMaxButtonPosition(float x) {
        if (x > seekBarRange + seekBarLeftOffset + seekBarButtonSize) {
            return seekBarRange + seekBarLeftOffset + seekBarButtonSize;
        } else if (x < minButton.getX() + seekBarButtonSize) {
            return minButton.getX() + seekBarButtonSize;
        } else {
            return x;
        }
    }

    private boolean isPointInsideView(float x, float y, View view) {
        Rect viewArea = new Rect();
        view.getGlobalVisibleRect(viewArea);
        return viewArea.contains((int) x, (int) y);
    }

    public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
        this.onValueChangedListener = onValueChangedListener;
    }

    private class MinInputListener extends InputTextFocusChangeListener {
        @Override
        void updateValue(int newValue) {
            if (newValue < minBound) {
                minValue = minBound;
            } else if (newValue > maxBound) {
                minValue = maxBound;
                maxValue = maxBound;
            } else if (newValue > maxValue) {
                minValue = newValue;
                maxValue = newValue;
            } else {
                minValue = newValue;
            }
        }
    }

    private class MaxInputListener extends InputTextFocusChangeListener {
        @Override
        void updateValue(int newValue) {
            if (newValue > maxBound) {
                maxValue = maxBound;
            } else if (newValue < minBound) {
                maxValue = minBound;
                minValue = minBound;
            } else if (newValue < minValue) {
                maxValue = newValue;
                minValue = newValue;
            } else {
                maxValue = newValue;
            }
        }
    }

    private abstract class InputTextFocusChangeListener implements OnFocusChangeListener {

        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (hasFocus) {
                return;
            }

            Editable editable = ((EditText) view).getText();
            int inputValue = 0;
            if (!TextUtils.isEmpty(editable.toString())) {
                inputValue = rupiahToInt(editable.toString());
            }
            updateValue(inputValue);
            refreshInputText();
            refreshButtonPosition();
        }

        private int rupiahToInt(String rupiah) {
            String cleanText = rupiah.replaceAll("[^0-9]", "");
            return Integer.parseInt(cleanText);
        }

        abstract void updateValue(int newValue);
    }

    public interface OnValueChangedListener {
        void onValueChanged(int minValue, int maxValue);
    }
}
