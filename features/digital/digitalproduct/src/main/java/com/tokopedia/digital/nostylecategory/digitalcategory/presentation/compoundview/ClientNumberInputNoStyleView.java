package com.tokopedia.digital.nostylecategory.digitalcategory.presentation.compoundview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.common_digital.product.presentation.compoundview.CommonClientNumberInputView;
import com.tokopedia.common_digital.product.presentation.model.AdditionalButton;
import com.tokopedia.common_digital.product.presentation.model.BaseWidgetItem;
import com.tokopedia.common_digital.product.presentation.model.ClientNumber;
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.Validation;
import com.tokopedia.digital.R;
import com.tokopedia.digital.utils.DeviceUtil;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Rizky on 05/09/18.
 */
public class ClientNumberInputNoStyleView extends CommonClientNumberInputView {

    private MitraClientNumberActionListener actionListener;

    private List<BaseWidgetItem> items;

    public ClientNumberInputNoStyleView(Context context) {
        super(context);
    }

    public ClientNumberInputNoStyleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClientNumberInputNoStyleView(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
    }

    public void setActionListener(MitraClientNumberActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    protected TextWatcher getTextWatcherInput(ClientNumber clientNumber) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String tempInput = charSequence.toString();
                getBtnClear().setVisibility(tempInput.length() > 0 ? VISIBLE : GONE);
                getTvErrorClientNumber().setText("");
                getTvErrorClientNumber().setVisibility(GONE);
                if (tempInput.isEmpty()) {
                    resetOperator();
                } else {
                    if (clientNumber.getType().equals(ClientNumberType.TYPE_INPUT_TEL)) {
                        if (tempInput.length() >= 4) {
                            String validClientNumber = DeviceUtil.validatePrefixClientNumber(tempInput);
                            boolean operatorFound = false;
                            outerLoop:
                            for (BaseWidgetItem baseWidgetItem : items) {
                                Operator operator = (Operator) baseWidgetItem;
                                for (String prefix : operator.getPrefixList()) {
                                    if (validClientNumber.startsWith(prefix)) {
                                        setOperator(operator);
                                        operatorFound = true;
                                        break outerLoop;
                                    }
                                }
                            }
                            if (!operatorFound) {
                                resetOperator();
                            }
                        } else {
                            resetOperator();
                        }
                    } else {
                        // TODO: add checking for additional button with type "inquiry"
                        if (clientNumber.getAdditionalButton() != null &&
                                clientNumber.getAdditionalButton().getType().equals("inquiry")) {
                            if (isValidInput()) {
                                getButtonAditional().setBackground(getResources().getDrawable(R.drawable.bg_button_green_enabled));
                                getButtonAditional().setClickable(true);
                            } else {
                                disableAditionalButton();
                                getButtonAditional().setBackground(getResources().getDrawable(R.drawable.digital_bg_button_disable));
                                getButtonAditional().setClickable(false);
                            }
                        }
                    }
                }
            }

            private void disableAditionalButton() {
                getButtonAditional().setBackground(getResources().getDrawable(R.drawable.digital_bg_button_disable));
                getButtonAditional().setClickable(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    @NonNull
    protected OnClickListener getButtonContactPickerClickListener() {
        return v -> actionListener.onButtonContactPickerClicked();
    }

    @NonNull
    protected OnClickListener getButtonClearClickListener() {
        return v -> {
            actionListener.onClientNumberCleared();
            getAutoCompleteTextView().setText("");
        };
    }

    @Override
    protected OnFocusChangeListener getFocusChangeListener() {
        return (view, hasFocus) -> {
            if (hasFocus) {
                actionListener.onClientNumberHasFocus(((TextView) view).getText().toString());
                if (getAutoCompleteTextView().getText().length() > 0) {
                    setBtnClearVisible();
                } else {
                    setBtnClearInvisible();
                }
            }
        };
    }

    private void resetOperator() {
        disableImageOperator();
        actionListener.onOperatorByPrefixNotFound();
    }

    private void setOperator(Operator operator) {
        getTvErrorClientNumber().setText("");
        getTvErrorClientNumber().setVisibility(GONE);
        enableImageOperator(operator.getImage());
        setFilterMaxLength(operator.getRule().getMaximumLength());
        actionListener.onOperatorFoundByPrefix(operator);
    }

    public void renderData(ClientNumber clientNumber, List<BaseWidgetItem> operators) {
        this.items = operators;
        this.setClientNumber(clientNumber);
        if (!TextUtils.isEmpty(clientNumber.getText())) {
            getTvLabel().setVisibility(VISIBLE);
            getTvLabel().setText(clientNumber.getText());
        } else {
            getTvLabel().setVisibility(GONE);
        }
        if (clientNumber.getAdditionalButton() != null) {
            if (clientNumber.getAdditionalButton().getType().equals("inquiry")) {
                getButtonAditional().setVisibility(VISIBLE);
                getButtonAditional().setOnClickListener(view -> {
                    actionListener.onClickAdditionalButton(clientNumber.getAdditionalButton());
                    getButtonAditional().setBackground(getResources().getDrawable(R.drawable.digital_bg_button_disable));
                    getButtonAditional().setClickable(false);
                });
            }
        }
        getTvLabel().setText(clientNumber.getText());
        getAutoCompleteTextView().setHint(clientNumber.getPlaceholder());
        setupLayoutParamAndInputType(clientNumber) ;

        final TextWatcher textWatcher = getTextWatcherInput(clientNumber);
        getAutoCompleteTextView().removeTextChangedListener(textWatcher);
        getAutoCompleteTextView().setOnFocusChangeListener(getFocusChangeListener());
        getAutoCompleteTextView().addTextChangedListener(textWatcher);
        getBtnClear().setOnClickListener(getButtonClearClickListener());
        getBtnContactPicker().setOnClickListener(getButtonContactPickerClickListener());
    }

    @Override
    protected void setupLayoutParamAndInputType(ClientNumber clientNumber) {
        LayoutParams layoutParams = new LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (clientNumber.getType().equalsIgnoreCase(ClientNumberType.TYPE_INPUT_TEL)) {
            getBtnContactPicker().setVisibility(View.VISIBLE);
            layoutParams.weight = 0.88f;
        } else {
            getBtnContactPicker().setVisibility(View.GONE);
            layoutParams.weight = 1;
        }
        getPulsaFramelayout().setLayoutParams(layoutParams);
        if (clientNumber.getType().equalsIgnoreCase(ClientNumberType.TYPE_INPUT_TEL)
                || clientNumber.getType().equalsIgnoreCase(ClientNumberType.TYPE_INPUT_NUMERIC)) {
            setInputTypeNumber();
        } else {
            setInputTypeText();
        }
    }

    private boolean isValidInput() {
        String clientNumberInput = getAutoCompleteTextView().getText().toString();
        if (getClientNumber() != null) {
            boolean isValidRegex = false;
            for (Validation validation : getClientNumber().getValidation()) {
                if (Pattern.matches(validation.getRegex(), clientNumberInput))
                    isValidRegex = true;
            }
            return isValidRegex;
        } else {
            return true;
        }
    }

    interface MitraClientNumberActionListener extends ActionListener {

        void onOperatorFoundByPrefix(Operator operator);

        void onOperatorByPrefixNotFound();

        void onClickAdditionalButton(AdditionalButton additionalButton);

    }

}
