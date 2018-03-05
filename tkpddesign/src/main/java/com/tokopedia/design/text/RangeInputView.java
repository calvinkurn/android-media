package com.tokopedia.design.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.widget.TextViewCompat;
import android.text.Editable;
import android.text.InputFilter;
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
import com.tokopedia.design.utils.StringUtils;

/**
 * Created by henrypriyono on 8/14/17.
 */

public class RangeInputView extends BaseCustomView {

    private static final int VALUE_STOP_COUNT = 100;
    private static final double DEFAULT_POWER = 2;
    private static final float DEFAULT_ELEVATION = 8f;
    private static final float DEFAULT_EMPTY_ELEVATION = 0f;
    protected EditText minValueEditText;
    protected EditText maxValueEditText;
    protected int minValue = 0;
    protected int maxValue = 0;
    private TextView minLabelTextView;
    private TextView maxLabelTextView;
    private DynamicBackgroundSeekBar dynamicBackgroundSeekBar;
    private View minButton;
    private View maxButton;
    private OnValueChangedListener onValueChangedListener;
    private GestureListener gestureListener;
    private int seekBarButtonSize;
    private int minBound = 0;
    private int maxBound = 0;
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

    private boolean isValueTypable;
    private int labelTextAppearance;
    private int valueTextAppearance;
    private int valueMaxLength;

    public RangeInputView(@NonNull Context context) {
        super(context);
        init();
    }

    public RangeInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RangeInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.DecimalRangeInputView);
        try {
            minLabelText = styledAttributes.getString(R.styleable.DecimalRangeInputView_driv_label_min);
            maxLabelText = styledAttributes.getString(R.styleable.DecimalRangeInputView_driv_label_max);
            isValueTypable = styledAttributes.getBoolean(R.styleable.DecimalRangeInputView_driv_typable, true);
            labelTextAppearance = styledAttributes.getResourceId(R.styleable.DecimalRangeInputView_driv_label_text_appearance, 0);
            valueTextAppearance = styledAttributes.getResourceId(R.styleable.DecimalRangeInputView_driv_value_text_appearance, 0);
            valueMaxLength = styledAttributes.getInt(R.styleable.DecimalRangeInputView_driv_value_max_length,
                    getContext().getResources().getInteger(R.integer.max_length_price_input));
        } finally {
            styledAttributes.recycle();
        }
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            maxButton.setElevation(DEFAULT_ELEVATION);
            minButton.setElevation(DEFAULT_ELEVATION);
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

        minValueEditText.setOnFocusChangeListener(new MinInputListener());
        maxValueEditText.setOnFocusChangeListener(new MaxInputListener());

        minValueEditText.setEnabled(isValueTypable);
        maxValueEditText.setEnabled(isValueTypable);

        if (labelTextAppearance!= 0) {
            TextViewCompat.setTextAppearance(minLabelTextView, labelTextAppearance);
            TextViewCompat.setTextAppearance(maxLabelTextView, labelTextAppearance);
        }
        if (valueTextAppearance!= 0) {
            TextViewCompat.setTextAppearance(minValueEditText, valueTextAppearance);
            TextViewCompat.setTextAppearance(maxValueEditText, valueTextAppearance);
        }

        addLengthFilter (minValueEditText);
        addLengthFilter (maxValueEditText);

        if (!TextUtils.isEmpty(minLabelText)) {
            minLabelTextView.setText(minLabelText);
        }
        if (!TextUtils.isEmpty(maxLabelText)) {
            maxLabelTextView.setText(maxLabelText);
        }
    }

    private void addLengthFilter(EditText editText) {
        InputFilter[] editFilters = editText.getFilters();
        boolean hasOverwrite = false;
        for (int i=0, sizei = editFilters.length; i<sizei; i++) {
            if (editFilters[i] instanceof InputFilter.LengthFilter) {
                editFilters[i] = new InputFilter.LengthFilter(valueMaxLength);
                hasOverwrite = true;
                break;
            }
        }
        if (!hasOverwrite) {
            InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
            System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
            newFilters[editFilters.length] = new InputFilter.LengthFilter(valueMaxLength);
            editText.setFilters(newFilters);
        }
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

    public void setPower(double power) {
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

    protected void refreshInputText() {
        setValueText(minValue, maxValue);
        if (onValueChangedListener != null) {
            onValueChangedListener.onValueChanged(minValue, maxValue, minBound, maxBound);
        }
    }

    protected void setValueText(int minValue, int maxValue){
        minValueEditText.setText(String.valueOf(minValue));
        maxValueEditText.setText(String.valueOf(maxValue));
        minValueEditText.setSelection(minValueEditText.length());
        maxValueEditText.setSelection(maxValueEditText.length());
    }

    public EditText getMinValueEditText() {
        return minValueEditText;
    }

    public EditText getMaxValueEditText() {
        return maxValueEditText;
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        minButton.setElevation(DEFAULT_EMPTY_ELEVATION);
                    }
                } else if (isPointInsideView(event.getRawX(), event.getRawY(), maxButton)) {
                    isMaxButtonDragging = true;
                    isMinButtonDragging = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        maxButton.setElevation(DEFAULT_EMPTY_ELEVATION);
                    }
                }
                if (gestureListener != null) {
                    gestureListener.onButtonPressed();
                }
                break;
            case MotionEvent.ACTION_UP:
                isMinButtonDragging = false;
                isMaxButtonDragging = false;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    maxButton.setElevation(DEFAULT_ELEVATION);
                    minButton.setElevation(DEFAULT_ELEVATION);
                }
                if (gestureListener != null) {
                    gestureListener.onButtonRelease();
                }
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

    public void setGestureListener(GestureListener gestureListener) {
        this.gestureListener = gestureListener;
    }

    public interface OnValueChangedListener {
        void onValueChanged(int minValue, int maxValue, int minBound, int maxBound);
    }

    public interface GestureListener {
        void onButtonRelease();
        void onButtonPressed();
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
                inputValue = Integer.parseInt(StringUtils.omitNonNumeric(editable.toString()));
            }
            updateValue(inputValue);
            refreshInputText();
            refreshButtonPosition();
        }

        abstract void updateValue(int newValue);
    }
}
