package com.tokopedia.mitra.digitalcategory.presentation.compoundview;

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
import com.tokopedia.common_digital.product.presentation.model.ClientNumber;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.mitra.DeviceUtil;

import java.util.List;

/**
 * Created by Rizky on 05/09/18.
 */
public class MitraClientNumberInputView<T> extends CommonClientNumberInputView {

    private MitraClientNumberActionListener actionListener;

    private List<T> items;

    public MitraClientNumberInputView(Context context) {
        super(context);
    }

    public MitraClientNumberInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MitraClientNumberInputView(Context context, AttributeSet attrs, int defaultStyle) {
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
                btnClear.setVisibility(tempInput.length() > 0 ? VISIBLE : GONE);
                tvErrorClientNumber.setText("");
                tvErrorClientNumber.setVisibility(GONE);
                if (tempInput.isEmpty()) {
                    resetOperator();
                } else {
                    if (clientNumber.getType().equals(ClientNumber.TYPE_INPUT_TEL)) {
                        if (tempInput.length() >= 4) {
                            String validClientNumber = DeviceUtil.validatePrefixClientNumber(tempInput);
                            boolean operatorFound = false;
                            outerLoop:
                            for (Operator operator : (List<Operator>) items) {
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

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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

    @Override
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

    private void resetOperator() {
//        operatorSelected = null;
//        productSelected = null;
        disableImageOperator();
        actionListener.onOperatorNotFound();
//        clearHolder(holderChooserProduct);
//        clearHolder(holderAdditionalInfoProduct);
//        clearHolder(holderPriceInfoProduct);
    }

    private void setOperator(Operator operator) {
//        operatorSelected = operator;
        tvErrorClientNumber.setText("");
        tvErrorClientNumber.setVisibility(GONE);
        enableImageOperator(operator.getImage());
        setFilterMaxLength(operator.getRule().getMaximumLength());
//        if (operator.getRule().getProductViewStyle() == SINGLE_PRODUCT) {
//            renderDefaultProductSelected();
//        } else {
//            showProducts();
//        }
//        setBtnBuyDigitalText(operator.getRule().getButtonText());
        actionListener.onOperatorFoundByPrefix(operator);
    }

    public void renderData(ClientNumber clientNumber, List<T> operators) {
        this.items = operators;
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

    @Override
    protected void setupLayoutParamAndInputType(ClientNumber clientNumber) {
        LayoutParams layoutParams = new LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT);
        // TODO: add checking for type "inquiry"
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

    interface MitraClientNumberActionListener extends ActionListener {

        void onOperatorFoundByPrefix(Operator operator);

        void onOperatorNotFound();

    }

}
