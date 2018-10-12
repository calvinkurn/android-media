package com.tokopedia.digital.newcart.presentation.compoundview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.common_digital.cart.view.model.cart.UserInputPriceDigital;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.voucher.VoucherCartHachikoView;
import com.tokopedia.digital.R;
import com.tokopedia.digital.cart.presentation.compoundview.InputPriceHolderView;

public class DigitalCartCheckoutHolderView extends BaseCustomView {
    private LinearLayout inputPriceContainer;
    private InputPriceHolderView inputPriceHolderView;
    private VoucherCartHachikoView voucherCartHachikoView;
    private AppCompatTextView totalPaymentTextView;
    private AppCompatButton checkoutButton;

    private ActionListener actionListener;
    private InputPriceHolderView.ActionListener inputPriceListener;
    private VoucherCartHachikoView.ActionListener voucherActionListener;

    private long pricePlain = 0;

    public DigitalCartCheckoutHolderView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public DigitalCartCheckoutHolderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DigitalCartCheckoutHolderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_digital_cart_checkout_holder, this, true
        );
        inputPriceContainer = view.findViewById(R.id.input_price_container);
        inputPriceHolderView = view.findViewById(R.id.input_price_holder_view);
        voucherCartHachikoView = view.findViewById(R.id.voucher_cart_holder_view);
        totalPaymentTextView = view.findViewById(R.id.tv_total_payment);
        checkoutButton = view.findViewById(R.id.btn_checkout);

        initViewListener();
    }

    private void initViewListener() {
        checkoutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionListener != null) {
                    actionListener.onCheckoutButtonClicked();
                }
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setVoucherCartHachikoListener(VoucherCartHachikoView.ActionListener actionListener) {
        voucherCartHachikoView.setActionListener(actionListener);
    }

    public void setInputPriceHolderListener(InputPriceHolderView.ActionListener actionListener) {
        inputPriceHolderView.setActionListener(actionListener);
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void setInputPriceListener(InputPriceHolderView.ActionListener inputPriceListener) {
        this.inputPriceListener = inputPriceListener;
        inputPriceHolderView.setActionListener(this.inputPriceListener);
    }

    public void setVoucherActionListener(VoucherCartHachikoView.ActionListener voucherActionListener) {
        this.voucherActionListener = voucherActionListener;
        voucherCartHachikoView.setActionListener(voucherActionListener);
    }

    public void showHachikoCart() {
        voucherCartHachikoView.setVisibility(VISIBLE);
    }

    public void setHachikoPromoAndCouponLabel() {
        voucherCartHachikoView.setPromoAndCouponLabel();
    }

    public void setHachikoPromoLabelOnly() {
        voucherCartHachikoView.setPromoLabelOnly();
    }

    public void hideHachikoCart() {
        voucherCartHachikoView.setVisibility(GONE);
    }

    public void setHachikoCoupon(String title, String message, String voucherCode) {
        voucherCartHachikoView.setCoupon(title, message, voucherCode);
    }

    public void enableVoucherDiscount(long discountAmountPlain) {

    }

    public void disableVoucherDiscount() {

    }

    public void setVoucher(String voucherCode, String message) {
        voucherCartHachikoView.setVoucher(voucherCode, message);
    }

    public void renderInputPrice(String total, UserInputPriceDigital userInputPriceDigital) {
        inputPriceHolderView.setVisibility(VISIBLE);
        inputPriceHolderView.setInputPriceInfo(total, userInputPriceDigital.getMinPaymentPlain(),
                userInputPriceDigital.getMaxPaymentPlain());
        inputPriceHolderView.bindView(userInputPriceDigital.getMinPayment(),
                userInputPriceDigital.getMaxPayment());
    }

    public void renderCheckout(String price, String totalPrice, long pricePlain) {
        this.pricePlain = pricePlain;
        totalPaymentTextView.setText(price);
    }

    public void enableCheckoutButton() {
        checkoutButton.setEnabled(true);
    }

    public void disableCheckoutButton() {
        checkoutButton.setEnabled(false);
    }

    public interface ActionListener {

        void onCheckoutButtonClicked();
    }
}
