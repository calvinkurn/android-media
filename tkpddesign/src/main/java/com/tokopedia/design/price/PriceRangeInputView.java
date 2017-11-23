package com.tokopedia.design.price;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.text.watcher.NumberTextWatcher;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by henrypriyono on 8/14/17.
 */

public class PriceRangeInputView extends BaseCustomView {

    private static final int VALUE_STOP_COUNT = 100;
    private static final double DEFAULT_POWER = 2;

    private View rootView;
    private TextView minLabel;
    private TextView maxLabel;
    private EditText minValueInput;
    private EditText maxValueInput;
    private PriceSeekbarDynamicBackground seekbarDynamicBackground;
    private View minButton;
    private View maxButton;

    private OnValueChangedListener onValueChangedListener;
    private NumberTextWatcher minTextWatcher;
    private NumberTextWatcher maxTextWatcher;

    private int seekbarButtonSize;
    private int minBound = 0;
    private int maxBound = 0;
    private int minValue = 0;
    private int maxValue = 0;
    private float valueRange = 0;
    private float seekbarWidth = 0;
    private float seekbarRange = 0;
    private float seekbarLeftOffset = 0;
    private int valueList[] = new int[VALUE_STOP_COUNT];
    private double baseRange = 0;
    private double power = 0;

    private boolean isMinButtonDragging = false;
    private boolean isMaxButtonDragging = false;

    public PriceRangeInputView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PriceRangeInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PriceRangeInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        seekbarButtonSize = context.getResources().getDimensionPixelSize(R.dimen.price_seekbar_button_size);

        rootView = inflate(context, R.layout.widget_price_range_input_view, this);
        rootView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                seekbarLeftOffset = seekbarDynamicBackground.getX();
                seekbarWidth = seekbarDynamicBackground.getWidth();
                seekbarRange = seekbarWidth - 2 * seekbarButtonSize;
                refreshButtonPosition();
            }
        });

        seekbarDynamicBackground
                = (PriceSeekbarDynamicBackground) rootView.findViewById(R.id.seekbar_background);
        minLabel = (TextView) rootView.findViewById(R.id.min_label);
        maxLabel = (TextView) rootView.findViewById(R.id.max_label);
        minValueInput = (EditText) rootView.findViewById(R.id.min_value);
        maxValueInput = (EditText) rootView.findViewById(R.id.max_value);
        minButton = rootView.findViewById(R.id.min_button);
        maxButton = rootView.findViewById(R.id.max_button);

        minTextWatcher = new NumberTextWatcher(minValueInput);
        maxTextWatcher = new NumberTextWatcher(maxValueInput);

        minValueInput.addTextChangedListener(minTextWatcher);
        maxValueInput.addTextChangedListener(maxTextWatcher);
        minValueInput.setOnFocusChangeListener(new MinInputListener());
        maxValueInput.setOnFocusChangeListener(new MaxInputListener());
    }

    public void setData(String minText, String maxText,
                        int minBound, int maxBound,
                        int minValue, int maxValue) {
        setData(minText, maxText, minBound, maxBound, minValue, maxValue, 0);
    }

    public void setData(String minText, String maxText,
                        int minBound, int maxBound,
                        int minValue, int maxValue, double power) {
        minLabel.setText(minText);
        maxLabel.setText(maxText);

        this.minBound = minBound;
        this.maxBound = maxBound;
        this.minValue = minValue;
        this.maxValue = maxValue;

        if (power > 0) {
            this.power = power;
        } else {
            this.power = DEFAULT_POWER;
        }

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
        return getPositionFromValue(maxValue) + seekbarButtonSize;
    }

    private int getPositionFromValue(int value) {
        double base = Math.pow(value - minBound, 1 / power);
        double result = base / baseRange * seekbarRange;
        return (int) (result + seekbarLeftOffset);
    }

    private void refreshSeekbarBackground() {
        seekbarDynamicBackground
                .setFirstPointPercentage((minButton.getX() - seekbarLeftOffset) / seekbarWidth);
        seekbarDynamicBackground
                .setSecondPointPercentage((maxButton.getX() - seekbarLeftOffset) / seekbarWidth);
        seekbarDynamicBackground.invalidate();
    }

    private void refreshInputText() {
        disableTextWatcher();
        minValueInput.setText(formatToRupiah(minValue));
        maxValueInput.setText(formatToRupiah(maxValue));
        minValueInput.setSelection(minValueInput.length());
        maxValueInput.setSelection(maxValueInput.length());
        onValueChangedListener.onValueChanged(minValue, maxValue);
        enableTextWatcher();
    }

    private void disableTextWatcher() {
        minValueInput.removeTextChangedListener(minTextWatcher);
        maxValueInput.removeTextChangedListener(maxTextWatcher);
    }

    private void enableTextWatcher() {
        minValueInput.addTextChangedListener(minTextWatcher);
        maxValueInput.addTextChangedListener(maxTextWatcher);
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
        int maxValue = getValueFromPosition(maxButton.getX() - seekbarButtonSize);
        if (maxValue > minValue) {
            return maxValue;
        } else {
            return minValue;
        }
    }

    private int getValueFromPosition(float x) {
        float valueStopPosition = (x - seekbarLeftOffset) / seekbarRange * (VALUE_STOP_COUNT - 1);
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
                float targetX = event.getRawX() - seekbarButtonSize;
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
        } else if (x > maxButton.getX() - seekbarButtonSize) {
            return maxButton.getX() - seekbarButtonSize;
        } else {
            return x;
        }
    }

    private float normalizeMaxButtonPosition(float x) {
        if (x > seekbarRange + seekbarLeftOffset + seekbarButtonSize) {
            return seekbarRange + seekbarLeftOffset + seekbarButtonSize;
        } else if (x < minButton.getX() + seekbarButtonSize) {
            return minButton.getX() + seekbarButtonSize;
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
