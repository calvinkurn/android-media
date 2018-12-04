package com.tokopedia.design.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;


/**
 * @author by alvarisi on 10/25/17.
 */

public class NumberPickerWithCounterView extends BaseCustomView {
    private static final int DEFAULT_VALUE = 0;
    private static final int DEFAULT_MIN_VALUE = 0;
    private static final int DEFAULT_MAX_VALUE = 100;
    private static final boolean DEFAULT_ENABLED_VALUE = true;

    private AppCompatTextView numberInputView;
    protected AppCompatImageButton minusImageButton;
    protected AppCompatImageButton plusImageButton;

    private boolean enabled;
    protected int defaultValue;
    protected int number;
    protected int minValue;
    protected int maxValue;
    private OnPickerActionListener onPickerActionListener;

    public interface OnPickerActionListener {
        void onNumberChange(int num);
    }

    public NumberPickerWithCounterView(@NonNull Context context) {
        super(context);
        init();
    }

    public NumberPickerWithCounterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public NumberPickerWithCounterView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.NumberPickerWithCounterView);
        try {
            maxValue = styledAttributes.getInteger(R.styleable.NumberPickerWithCounterView_npc_max_value, DEFAULT_MAX_VALUE);
            minValue = styledAttributes.getInteger(R.styleable.NumberPickerWithCounterView_npc_min_value, DEFAULT_MIN_VALUE);
            number = styledAttributes.getInteger(R.styleable.NumberPickerWithCounterView_npc_value, DEFAULT_VALUE);
            enabled = styledAttributes.getBoolean(R.styleable.NumberPickerWithCounterView_npc_enabled, DEFAULT_ENABLED_VALUE);
        } finally {
            styledAttributes.recycle();
        }
    }

    protected void init() {
        View view = inflate(getContext(), R.layout.widget_number_picker_with_counter_view, this);

        plusImageButton = (AppCompatImageButton) view.findViewById(R.id.image_button_plus);
        minusImageButton = (AppCompatImageButton) view.findViewById(R.id.image_button_minus);
        numberInputView = (AppCompatTextView) view.findViewById(R.id.decimal_input_view);
    }

    protected void setEnabledNumberInputView(boolean enabled) {
        numberInputView.setEnabled(enabled);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setTextNumberInputView(number);
        plusImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                validateMaxValue(Integer.parseInt(getNumberTextString().trim()) + 1);
                updateButtonState();
            }
        });

        minusImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                validateMinValue(Integer.parseInt(getNumberTextString().trim()) - 1);
                updateButtonState();
            }
        });
        setEnabledNumberInputView(enabled);
        updateButtonState();
    }

    private void validateMaxValue(int newNumber) {
        if (Integer.parseInt(getNumberTextString().trim()) < maxValue) {
            setTextNumberInputView(newNumber);
        }
    }

    protected void setTextNumberInputView(int newNumber) {
        numberInputView.setText(String.valueOf(newNumber));
    }

    private void validateMinValue(int newNumber) {
        if (Integer.parseInt(getNumberTextString().trim()) > minValue) {
            setTextNumberInputView(newNumber);
        }
    }

    protected void updateButtonState() {
        minusImageButton.setEnabled(enabled && Integer.parseInt(getNumberTextString().trim()) > minValue);
        plusImageButton.setEnabled(enabled && Integer.parseInt(getNumberTextString().trim()) < maxValue);

        if (onPickerActionListener != null) {
            onPickerActionListener.onNumberChange(Integer.parseInt(getNumberTextString().trim()));
        }
    }

    public void setInitialState(int minValue, int maxValue, int defaultValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.defaultValue = defaultValue;
        setTextNumberInputView(defaultValue);
        minusImageButton.setEnabled(enabled && Integer.parseInt(getNumberTextString().trim()) > minValue);
        plusImageButton.setEnabled(enabled && Integer.parseInt(getNumberTextString().trim()) < maxValue);
        invalidate();
        requestLayout();
    }

    public void setOnPickerActionListener(OnPickerActionListener onPickerActionListener) {
        this.onPickerActionListener = onPickerActionListener;
    }

    public int getValue() {
        return Integer.parseInt(getNumberTextString().trim());
    }

    @NonNull
    protected String getNumberTextString() {
        return numberInputView.getText().toString();
    }
}
