package com.tokopedia.digital.common.view.compoundview;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.common_digital.product.presentation.model.ClientNumber;
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType;
import com.tokopedia.common_digital.product.presentation.model.Validation;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.adapter.DigitalAutoCompleteTVAdapter;
import com.tokopedia.digital.product.view.model.OrderClientNumber;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author anggaprasetiyo on 5/6/17.
 */
public class ClientNumberInputView extends LinearLayout {

    private TextView tvLabel;
    private AutoCompleteTextView autoCompleteTextView;
    private ImageView btnClear;
    private ImageView imgOperator;
    private Button btnContactPicker;
    private ImageView btnCameraPicker;
    private RelativeLayout pulsaFramelayout;
    private TextView tvErrorClientNumber;

    private ActionListener actionListener;
    private DigitalAutoCompleteTVAdapter digitalAutoCompleteTVAdapter;
    private ClientNumber clientNumber;

    public ClientNumberInputView(Context context) {
        super(context);
        init(context);
    }

    public ClientNumberInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClientNumberInputView(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_holder_digital_client_number_input, this, true);

        tvLabel = findViewById(R.id.tv_label_client_number);
        autoCompleteTextView = findViewById(R.id.ac_client_number);
        btnClear = findViewById(R.id.btn_clear_client_number);
        imgOperator = findViewById(R.id.iv_pic_operator);
        btnContactPicker = findViewById(R.id.btn_contact_picker);
        btnCameraPicker = findViewById(R.id.btn_camera_picker);
        pulsaFramelayout = findViewById(R.id.fl_holder_input_client_number);
        tvErrorClientNumber = findViewById(R.id.tv_error_client_number);

        initialTextWatcher();
        setImgOperatorInvisible();
        setBtnClearInvisible();
    }

    private void initialTextWatcher() {

    }

    private OnFocusChangeListener getFocusChangeListener() {
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

    public void setAdapterAutoCompleteClientNumber(List<OrderClientNumber> numberList) {
        digitalAutoCompleteTVAdapter = new DigitalAutoCompleteTVAdapter(getContext(), R.layout.view_digital_item_autocomplete, numberList);
        autoCompleteTextView.setAdapter(digitalAutoCompleteTVAdapter);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setOnItemClickListener(getItemClickListener());
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
        setImgOperator(imageUrl);
    }

    public void disableImageOperator() {
        imgOperator.setVisibility(GONE);
        setImgOperator("");
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
        ImageHandler.LoadImage(this.imgOperator, imgUrl);
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
        this.btnCameraPicker.setOnClickListener(getButtonCameraPickerClickListener());
    }

    private void setupLayoutParamAndInputType(ClientNumber clientNumber) {
        LayoutParams layoutParams = new LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (clientNumber.getType().equalsIgnoreCase(ClientNumberType.TYPE_INPUT_TEL)) {
            btnContactPicker.setVisibility(View.VISIBLE);
            layoutParams.weight = 0.88f;
        } else {
            btnContactPicker.setVisibility(View.GONE);
            if (clientNumber.isEmoney()) {
                btnCameraPicker.setVisibility(VISIBLE);
                layoutParams.weight = 0.88f;
            } else {
                btnCameraPicker.setVisibility(GONE);
                layoutParams.weight = 1;
            }
        }
        pulsaFramelayout.setLayoutParams(layoutParams);
        if (clientNumber.getType().equalsIgnoreCase(ClientNumberType.TYPE_INPUT_TEL)
                || clientNumber.getType().equalsIgnoreCase(ClientNumberType.TYPE_INPUT_NUMERIC)) {
            setInputTypeNumber();
        } else {
            setInputTypeText();
        }
    }

    private AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderClientNumber orderClientNumber = digitalAutoCompleteTVAdapter.getItem(position);
                actionListener.onItemAutocompletedSelected(orderClientNumber);
            }
        };
    }

    @NonNull
    private OnClickListener getButtonContactPickerClickListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onButtonContactPickerClicked();
            }
        };
    }

    @NonNull
    private OnClickListener getButtonCameraPickerClickListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onButtonCameraPickerClicked();
            }
        };
    }

    @NonNull
    private OnClickListener getButtonClearClickListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onClientNumberCleared();
                autoCompleteTextView.setText("");
            }
        };
    }

    @NonNull
    private TextWatcher getTextWatcherInput(final ClientNumber clientNumber) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String tempInput = charSequence.toString();
                btnClear.setVisibility(tempInput.length() > 0 ? VISIBLE : GONE);
                tvErrorClientNumber.setText("");
                tvErrorClientNumber.setVisibility(GONE);
                if (tempInput.isEmpty()) {
                    actionListener.onClientNumberInputInvalid();
                } else {
                    actionListener.onClientNumberInputValid(tempInput);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    public boolean isValidInput(List<String> prefixList) {
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

    public void hideClientNumberError() {
        tvErrorClientNumber.setText("");
        tvErrorClientNumber.setVisibility(GONE);
    }

    public interface ActionListener {
        void onButtonCameraPickerClicked();

        void onButtonContactPickerClicked();

        void onClientNumberInputValid(String tempClientNumber);

        void onClientNumberInputInvalid();

        void onClientNumberHasFocus(String clientNumber);

        void onClientNumberCleared();

        void onItemAutocompletedSelected(OrderClientNumber orderClientNumber);
    }

}