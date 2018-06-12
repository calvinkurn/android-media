package com.tokopedia.design.voucher;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;

/**
 * Created by nabillasabbaha on 11/20/17.
 * this custom view is for voucher view in cart
 */

public class VoucherCartView extends BaseCustomView {

    private CheckBox checkBoxVoucher;
    private RelativeLayout holderInputVoucher;
    private RelativeLayout holderVoucher;
    private TextView buttonVoucher;
    private TextView buttonCancel;
    private EditText editTextVoucher;
    private TextView errorVoucher;
    private TextView usedVoucher;
    private TextView labelUsedVoucher;

    private View rootView;
    private ActionListener actionListener;
    private String voucherCode = "";

    public VoucherCartView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public VoucherCartView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VoucherCartView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setLayout(context);
        checkBoxVoucher = (CheckBox) rootView.findViewById(R.id.checkbox_voucher);
        holderInputVoucher = (RelativeLayout) rootView.findViewById(R.id.holder_input_voucher);
        holderVoucher = (RelativeLayout) rootView.findViewById(R.id.holder_voucher);
        buttonVoucher = (TextView) rootView.findViewById(R.id.button_voucher);
        buttonCancel = (TextView) rootView.findViewById(R.id.button_cancel);
        editTextVoucher = (EditText) rootView.findViewById(R.id.edittext_voucher);
        errorVoucher = (TextView) rootView.findViewById(R.id.error_voucher);
        usedVoucher = (TextView) rootView.findViewById(R.id.textview_voucher);
        labelUsedVoucher = (TextView) rootView.findViewById(R.id.text_checkedbox);

        actionVoucher();
    }

    private void setLayout(Context context) {
        rootView = inflate(context, R.layout.widget_voucher_cart, this);
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    private void actionVoucher() {
        checkBoxVoucher.setOnCheckedChangeListener(getCheckboxVoucherListener());
        labelUsedVoucher.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxVoucher.setChecked(!(checkBoxVoucher.isChecked()));
            }
        });
        buttonVoucher.setOnClickListener(getVoucherListener());
        buttonCancel.setOnClickListener(getCancelVoucherListener());
    }

    private CompoundButton.OnCheckedChangeListener getCheckboxVoucherListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    if (actionListener != null) {
                        hideHolderVoucher();
                        editTextVoucher.setText("");
                        errorVoucher.setVisibility(GONE);
                        actionListener.forceHideSoftKeyboardVoucherInput();
                        actionListener.disableVoucherDiscount();
                    }
                } else {
                    editTextVoucher.requestFocus();
                    if (actionListener != null) actionListener.forceShowSoftKeyboardVoucherInput();
                }
                holderInputVoucher.setVisibility(isChecked ? VISIBLE : GONE);
            }
        };
    }

    private OnClickListener getVoucherListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditTextVoucherEmpty()) {
                    if (actionListener != null) {
                        voucherCode = editTextVoucher.getText().toString().trim();
                        actionListener.onVoucherCheckButtonClicked();
                    } else throw new IllegalArgumentException("Action Listener null coy!!");
                } else {
                    hideHolderVoucher();
                    errorVoucher.setText(getContext().getString(R.string.empty_voucher));
                    errorVoucher.setVisibility(VISIBLE);
                }
            }
        };
    }

    public String getVoucherCode() {
        if (TextUtils.isEmpty(voucherCode) && checkBoxVoucher.isChecked())
            checkBoxVoucher.setChecked(false);
        return voucherCode;
    }

    /**
     *
     * @param voucherName voucher code
     * @param message message that has been show up when success/failed verify voucher code
     */

    public void setUsedVoucher(String voucherName, String message) {
        actionListener.trackingSuccessVoucher(voucherName);
        voucherCode = voucherName;
        usedVoucher.setText(message);
        holderVoucher.setVisibility(VISIBLE);
        errorVoucher.setVisibility(GONE);
    }

    /**
     *
     * @param errorMessage
     */

    public void setErrorVoucher(String errorMessage) {
        actionListener.trackingErrorVoucher(errorMessage);
        hideHolderVoucher();
        errorVoucher.setVisibility(VISIBLE);
        errorVoucher.setText(errorMessage);
        actionListener.disableVoucherDiscount();
    }

    private boolean isEditTextVoucherEmpty() {
        return TextUtils.isEmpty(editTextVoucher.getText().toString());
    }

    private OnClickListener getCancelVoucherListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.trackingCancelledVoucher();
                hideHolderVoucher();
                editTextVoucher.setText("");
                checkBoxVoucher.setChecked(false);
            }
        };
    }

    private void hideHolderVoucher() {
        voucherCode = "";
        holderVoucher.setVisibility(GONE);
    }

    /**
     *
     * @param voucherAutoCode voucher code
     */

    public void renderVoucherAutoCode(String voucherAutoCode) {
        if (!TextUtils.isEmpty(voucherAutoCode)) {
            checkBoxVoucher.setChecked(true);
            editTextVoucher.setText(voucherAutoCode);
            voucherCode = voucherAutoCode;
            actionListener.onVoucherCheckButtonClicked();
        }
    }

    public interface ActionListener {
        void onVoucherCheckButtonClicked();

        void forceHideSoftKeyboardVoucherInput();

        void forceShowSoftKeyboardVoucherInput();

        void disableVoucherDiscount();

        void trackingErrorVoucher(String errorMsg);

        void trackingSuccessVoucher(String voucherName);

        void trackingCancelledVoucher();
    }
}
