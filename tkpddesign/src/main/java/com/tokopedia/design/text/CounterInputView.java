package com.tokopedia.design.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;

/**
 * Created by nathan on 04/05/17.
 */

public class CounterInputView extends BaseCustomView {

    private static final int DEFAULT_MIN_VALUE = 0;
    private static final int DEFAULT_INPUT_VALUE_LENGTH = -1;

    private DecimalInputView decimalInputView;
    private ImageButton minusImageButton;
    private ImageButton plusImageButton;

    private String hintText;
    private String valueText;
    private int minValue;
    private boolean showCounterButton;
    private boolean enabled;
    private int maxLength;

    public CounterInputView(Context context) {
        super(context);
        init();
    }

    public CounterInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CounterInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.CounterInputView);
        try {
            maxLength = styledAttributes.getInt(R.styleable.CounterInputView_counter_max_length, DEFAULT_INPUT_VALUE_LENGTH);
            hintText = styledAttributes.getString(R.styleable.CounterInputView_counter_hint);
            valueText = styledAttributes.getString(R.styleable.CounterInputView_counter_text);
            minValue = styledAttributes.getInt(R.styleable.CounterInputView_counter_min, DEFAULT_MIN_VALUE);
            showCounterButton = styledAttributes.getBoolean(R.styleable.CounterInputView_counter_show_counter_button, true);
            enabled = styledAttributes.getBoolean(R.styleable.CounterInputView_counter_enabled, true);
        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        decimalInputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        minusImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    long result = (long) decimalInputView.getDoubleValue() - 1;
                    if (result >= 0) {
                        decimalInputView.setText(String.valueOf(result));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        plusImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                decimalInputView.setText(String.valueOf((long) decimalInputView.getDoubleValue() + 1));
            }
        });

        if (!TextUtils.isEmpty(hintText)) {
            decimalInputView.setHint(hintText);
        }
        if (!TextUtils.isEmpty(valueText)) {
            decimalInputView.setText(valueText);
        }
        setMaxLength(maxLength);
        updateCounterButtonView(showCounterButton);
        setEnabled(enabled);
        updateButtonState();
        invalidate();
        requestLayout();
    }

    public void updateCounterButtonView(boolean show) {
        plusImageButton.setVisibility(show ? View.VISIBLE : View.GONE);
        minusImageButton.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_count_input_view, this);
        decimalInputView = (DecimalInputView) view.findViewById(R.id.decimal_input_view);
        minusImageButton = (ImageButton) view.findViewById(R.id.image_button_minus);
        plusImageButton = (ImageButton) view.findViewById(R.id.image_button_plus);

    }

    @Override
    public void setEnabled(boolean enable) {
        this.enabled = enable;
        decimalInputView.setEnabled(enable);
        plusImageButton.setEnabled(enable);
        updateButtonState();
    }

    public void updateMinusButtonState(boolean enable) {
        minusImageButton.setEnabled(enable);
    }

    private void updateButtonState() {
        if (enabled) {
            minusImageButton.setEnabled(decimalInputView.getDoubleValue() > minValue);
        } else {
            minusImageButton.setEnabled(false);
        }
    }

    public void setHint(String hintText) {
        decimalInputView.setHint(hintText);
        invalidate();
        requestLayout();
    }

    public void setText(String textValue) {
        decimalInputView.setText(textValue);
        invalidate();
        requestLayout();
    }

    public void showCounterButton(boolean show) {
        updateCounterButtonView(show);
        invalidate();
        requestLayout();
    }

    public void addTextChangedListener(TextWatcher watcher) {
        decimalInputView.addTextChangedListener(watcher);
    }

    public void removeTextChangedListener(TextWatcher watcher) {
        decimalInputView.removeTextChangedListener(watcher);
    }

    public void removeDefaultTextWatcher(){
        decimalInputView.removeDefaultTextWatcher();
    }

    public void setError(String error) {
        decimalInputView.setError(error);
    }

    public String getValueText() {
        return decimalInputView.getText();
    }

    public double getDoubleValue() {
        return decimalInputView.getDoubleValue();
    }

    public void setValue(double value) {
        decimalInputView.setValue(value);
    }

    public EditText getEditText() {
        return decimalInputView.getEditText();
    }

    public void setMaxLength(int maxLengthInput) {
        if(maxLengthInput > DEFAULT_INPUT_VALUE_LENGTH){
            decimalInputView.setMaxLength(maxLengthInput);
        }
    }
}