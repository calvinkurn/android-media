package com.tokopedia.digital.cart.compoundview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.drew.lang.annotations.NotNull;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author by Nabilla Sabbaha on 2/27/2017.
 */

public class VoucherCartHolderView extends RelativeLayout {

    @BindView(R2.id.checkbox_voucher)
    CheckBox checkBoxVoucher;
    @BindView(R2.id.holder_input_voucher)
    RelativeLayout holderInputVoucher;
    @BindView(R2.id.holder_voucher)
    RelativeLayout holderVoucher;
    @BindView(R2.id.button_voucher)
    TextView buttonVoucher;
    @BindView(R2.id.button_cancel)
    TextView buttonCancel;
    @BindView(R2.id.edittext_voucher)
    EditText editTextVoucher;
    @BindView(R2.id.error_voucher)
    TextView errorVoucher;
    @BindView(R2.id.textview_voucher)
    TextView usedVoucher;
    @BindView(R2.id.text_checkedbox)
    TextView labelUsedVoucher;

    private ActionListener actionListener;
    private String voucherCode = "";
    private Context context;

    public VoucherCartHolderView(Context context) {
        super(context);
        init(context);
    }

    public VoucherCartHolderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VoucherCartHolderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_holder_checkout_voucher_digital_module, this, true);
        ButterKnife.bind(this);
        actionVoucher();
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

    @NotNull
    private CompoundButton.OnCheckedChangeListener getCheckboxVoucherListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    if (actionListener != null) {
                        hideHolderVoucher();
                        editTextVoucher.setText("");
                        actionListener.forceHideSoftKeyboardVoucherInput();
                    }
                } else {
                    editTextVoucher.requestFocus();
                    if (actionListener != null) actionListener.forceShowSoftKeyboardVoucherInput();
                }
                holderInputVoucher.setVisibility(isChecked ? VISIBLE : GONE);
            }
        };
    }

    @NotNull
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
                    errorVoucher.setText(context.getString(R.string.empty_voucher));
                    errorVoucher.setVisibility(VISIBLE);
                }
            }
        };
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setUsedVoucher(String voucherName, String message) {
        voucherCode = voucherName;
        usedVoucher.setText(message);
        holderVoucher.setVisibility(VISIBLE);
        errorVoucher.setVisibility(GONE);
    }

    public void setErrorVoucher(String errorMessage) {
        hideHolderVoucher();
        errorVoucher.setVisibility(VISIBLE);
        errorVoucher.setText(errorMessage);
    }

    private boolean isEditTextVoucherEmpty() {
        return TextUtils.isEmpty(editTextVoucher.getText().toString());
    }

    @NotNull
    private OnClickListener getCancelVoucherListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideHolderVoucher();
                editTextVoucher.setText("");

            }
        };
    }

    private void hideHolderVoucher() {
        voucherCode = "";
        holderVoucher.setVisibility(GONE);
    }

    public interface ActionListener {
        void onVoucherCheckButtonClicked();

        void forceHideSoftKeyboardVoucherInput();

        void forceShowSoftKeyboardVoucherInput();
    }
}