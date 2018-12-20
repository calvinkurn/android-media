package com.tokopedia.common_digital.product.presentation.compoundview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.common_digital.R;
import com.tokopedia.common_digital.product.presentation.model.ClientNumber;
import com.tokopedia.common_digital.product.presentation.model.Validation;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author anggaprasetiyo on 5/6/17.
 */
public abstract class CommonClientNumberInputView extends LinearLayout {

    protected TextView tvLabel;
    protected AutoCompleteTextView autoCompleteTextView;
    protected Button btnClear;
    protected ImageView imgOperator;
    protected Button btnContactPicker;
    protected RelativeLayout pulsaFramelayout;
    protected TextView tvErrorClientNumber;
    protected Button buttonAditional;

    private ActionListener actionListener;
    private Context context;

    protected ClientNumber clientNumber;

    public CommonClientNumberInputView(Context context) {
        super(context);
        init(context);
    }

    public CommonClientNumberInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommonClientNumberInputView(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        View view = LayoutInflater.from(context).inflate(R.layout.view_holder_common_client_number_input, this, true);
        tvLabel = view.findViewById(R.id.tv_label_client_number);
        autoCompleteTextView = view.findViewById(R.id.ac_client_number);
        btnClear = view.findViewById(R.id.btn_clear_client_number);
        imgOperator = view.findViewById(R.id.iv_pic_operator);
        btnContactPicker = view.findViewById(R.id.btn_contact_picker);
        pulsaFramelayout = view.findViewById(R.id.fl_holder_input_client_number);
        tvErrorClientNumber = view.findViewById(R.id.tv_error_client_number);
        buttonAditional = view.findViewById(R.id.button_additional);

        initialTextWatcher();
        setImgOperatorInvisible();
        setBtnClearInvisible();
    }

    private void initialTextWatcher() {

    }

    protected OnFocusChangeListener getFocusChangeListener() {
        return (view, hasFocus) -> {
            if (hasFocus) {
                actionListener.onClientNumberHasFocus(((TextView) view).getText().toString());
                if (autoCompleteTextView.getText().length() > 0) {
                    setBtnClearVisible();
                } else {
                    setBtnClearInvisible();
                }
            }
        };
    }

    public String getText() {
        return this.autoCompleteTextView.getText().toString();
    }

    public void setText(String text) {
        this.autoCompleteTextView.setText(text);
    }

    public void setErrorText(String errorMessage) {
        tvErrorClientNumber.setText(errorMessage);
        tvErrorClientNumber.setVisibility(VISIBLE);
    }

    public void setHint(String hint) {
        this.autoCompleteTextView.setHint(hint);
    }

    public void setImgOperatorInvisible() {
        this.imgOperator.setVisibility(GONE);
    }

    public void setBtnClearVisible() {
        this.btnClear.setVisibility(VISIBLE);
    }

    public void setBtnClearInvisible() {
        this.btnClear.setVisibility(GONE);
    }

    public void enableImageOperator(String imageUrl) {
        imgOperator.setVisibility(VISIBLE);
        Glide.with(context).load(imageUrl).dontAnimate().into(this.imgOperator);
    }

    public void disableImageOperator() {
        imgOperator.setVisibility(GONE);
        Glide.with(context).load("").dontAnimate().into(this.imgOperator);
    }

    public void setInputTypeNumber() {
        this.autoCompleteTextView.setInputType(InputType.TYPE_CLASS_NUMBER);
        this.autoCompleteTextView.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
    }

    public void setInputTypeText() {
        this.autoCompleteTextView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
    }

    public void setFilterMaxLength(int maximumLength) {
        this.autoCompleteTextView.setFilters(
                new InputFilter[]{new InputFilter.LengthFilter(maximumLength)});
    }

    public void setImgOperator(String imgUrl) {
        Glide.with(getContext()).load(imgUrl).dontAnimate().into(this.imgOperator);
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void renderData(final ClientNumber clientNumber) {
        this.clientNumber = clientNumber;
        if (!TextUtils.isEmpty(clientNumber.getText())) {
            tvLabel.setVisibility(VISIBLE);
            tvLabel.setText(clientNumber.getText());
        } else {
            tvLabel.setVisibility(GONE);
        }
        tvLabel.setText(clientNumber.getText());
        autoCompleteTextView.setHint(clientNumber.getPlaceholder());
        setupLayoutParamAndInputType(clientNumber);
        autoCompleteTextView.setOnFocusChangeListener(getFocusChangeListener());
        final TextWatcher textWatcher = getTextWatcherInput(clientNumber);
        autoCompleteTextView.removeTextChangedListener(textWatcher);
        autoCompleteTextView.addTextChangedListener(textWatcher);
        this.btnClear.setOnClickListener(getButtonClearClickListener());
        this.btnContactPicker.setOnClickListener(getButtonContactPickerClickListener());
    }

    protected void setupLayoutParamAndInputType(ClientNumber clientNumber) {
        LayoutParams layoutParams = new LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (clientNumber.getType().equalsIgnoreCase(ClientNumber.TYPE_INPUT_TEL)) {
            btnContactPicker.setVisibility(View.VISIBLE);
            layoutParams.weight = 0.88f;
        } else {
            btnContactPicker.setVisibility(View.GONE);
            layoutParams.weight = 1;
        }
        pulsaFramelayout.setLayoutParams(layoutParams);
        if (clientNumber.getType().equalsIgnoreCase(ClientNumber.TYPE_INPUT_TEL)
                || clientNumber.getType().equalsIgnoreCase(ClientNumber.TYPE_INPUT_NUMERIC)) {
            setInputTypeNumber();
        } else {
            setInputTypeText();
        }
    }

    @NonNull
    protected OnClickListener getButtonContactPickerClickListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onButtonContactPickerClicked();
            }
        };
    }

    @NonNull
    protected OnClickListener getButtonClearClickListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onClientNumberCleared();
                autoCompleteTextView.setText("");
            }
        };
    }

    @NonNull
    protected abstract TextWatcher getTextWatcherInput(final ClientNumber clientNumber);

    protected boolean isValidInput(List<String> prefixList) {
        String clientNumberInput = autoCompleteTextView.getText().toString();
        boolean isStartWithPrefix = false;
        for (String string : prefixList) {
            if (clientNumberInput.startsWith(string)) {
                isStartWithPrefix = true;
                break;
            }
        }
        if (!isStartWithPrefix) return false;
        if (clientNumber != null) {
            boolean isValidRegex = false;
            for (Validation validation : clientNumber.getValidation()) {
                if (Pattern.matches(validation.getRegex(), clientNumberInput))
                    isValidRegex = true;
            }
            return isValidRegex;
        } else {
            return true;
        }
    }

    public void resetInputTyped() {
        autoCompleteTextView.setText("");
    }

    public interface ActionListener {

        void onButtonContactPickerClicked();

        void onClientNumberHasFocus(String clientNumber);

        void onClientNumberCleared();

    }

}