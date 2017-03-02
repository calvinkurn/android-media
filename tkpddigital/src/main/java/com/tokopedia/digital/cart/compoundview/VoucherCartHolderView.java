package com.tokopedia.digital.cart.compoundview;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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
 * Created by Nabilla Sabbaha on 2/27/2017.
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
        LayoutInflater.from(context).inflate(R.layout.view_holder_checkout_voucher, this, true);
        ButterKnife.bind(this);
        actionVoucher();
    }

    private void actionVoucher() {
        checkBoxVoucher.setOnCheckedChangeListener(getCheckboxVoucherListener());
        buttonVoucher.setOnClickListener(getVoucherListener());
        buttonCancel.setOnClickListener(getCancelVoucherListener());
        editTextVoucher.addTextChangedListener(onEditTextChangeListener());
    }

    @NotNull
    private CompoundButton.OnCheckedChangeListener getCheckboxVoucherListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
                    usedVoucher.setText(editTextVoucher.getText().toString());
                    holderVoucher.setVisibility(VISIBLE);
                    errorVoucher.setVisibility(GONE);
                } else {
                    errorVoucher.setVisibility(VISIBLE);
                }
            }
        };
    }

    private boolean isEditTextVoucherEmpty() {
        return editTextVoucher.getText().toString().equals("");
    }

    @NotNull
    private OnClickListener getCancelVoucherListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                holderVoucher.setVisibility(GONE);
                editTextVoucher.setText("");
            }
        };
    }

    private TextWatcher onEditTextChangeListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    errorVoucher.setVisibility(GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }
}