package com.tokopedia.digital.cart.compoundview;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;

/**
 * Created by nabillasabbaha on 11/20/17.
 * this custom view is for voucher view in cart
 */

public class VoucherCartHolderView extends RelativeLayout {

    @BindView(R2.id.textview_voucher)
    TextView labelUseVoucher;
    @BindView(R2.id.label_promo_code)
    TextView labelPromoCode;
    @BindView(R2.id.textview_promo_code)
    TextView textviewPromoCode;
    @BindView(R2.id.textview_voucher_detail)
    TextView textviewVoucherDetail;
    @BindView(R2.id.button_cancel)
    ImageView buttonCancel;
    @BindView(R2.id.cardview_used_promo)
    CardView cardUsedPromo;
    @BindView(R2.id.cardview_input_promo)
    CardView cardInputPromo;

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
        labelUseVoucher.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onClickUseVoucher();
            }
        });

        buttonCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelVoucher();
                actionListener.onClickCloseButton();
            }
        });
    }

    private void cancelVoucher() {
        voucherCode = "";
        cardUsedPromo.setVisibility(GONE);
        cardInputPromo.setVisibility(VISIBLE);
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setPromo(String voucherName, String message) {
        UnifyTracking.eventVoucherSuccess(voucherCode,"");

        cardInputPromo.setVisibility(GONE);

        voucherCode = voucherName;
        labelPromoCode.setText("Kode Promo: ");
        textviewPromoCode.setText(voucherName);
        textviewVoucherDetail.setText(message);

        cardUsedPromo.setVisibility(VISIBLE);
    }

//    private boolean isEditTextVoucherEmpty() {
//        return TextUtils.isEmpty(editTextVoucher.getText().toString());
//    }

//    @NotNull
//    private OnClickListener getCancelVoucherListener() {
//        return new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UnifyTracking.eventClickCancelVoucher("","");
//                hideHolderVoucher();
//                editTextVoucher.setText("");
//                checkBoxVoucher.setChecked(false);
//            }
//        };
//    }

    public void renderVoucherAutoCode(String voucherAutoCode) {
        if (!TextUtils.isEmpty(voucherAutoCode)) {
            voucherCode = voucherAutoCode;
//            actionListener.onVoucherCheckButtonClicked();
        }
    }

    public interface ActionListener {
        void disableVoucherDiscount();

        void onClickUseVoucher();

        void onClickCloseButton();
    }
}