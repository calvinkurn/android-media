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

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.voucher.VoucherCartHachikoView;
import com.tokopedia.digital.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class DigitalCartCheckoutHolderView extends BaseCustomView {

    private VoucherCartHachikoView voucherCartHachikoView;
    private AppCompatTextView totalPaymentTextView;
    private AppCompatButton checkoutButton;
    private LinearLayout checkoutContainer;

    private ActionListener actionListener;

    private long pricePlain = 0;
    private long voucherDiscount = 0;

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

        voucherCartHachikoView = view.findViewById(R.id.voucher_cart_holder_view);
        totalPaymentTextView = view.findViewById(R.id.tv_total_payment);
        checkoutButton = view.findViewById(R.id.btn_checkout);
        checkoutContainer = view.findViewById(R.id.container_checkout);

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

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void setVoucherActionListener(VoucherCartHachikoView.ActionListener voucherActionListener) {
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

    public void enableVoucherDiscount(long voucherDiscount) {
        this.voucherDiscount = voucherDiscount;
        long totalPrice = pricePlain - voucherDiscount;
        totalPaymentTextView.setText(getStringIdrFormat((double) totalPrice));
    }

    public String getStringIdrFormat(Double value) {
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        kursIndonesia.setMaximumFractionDigits(0);
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp ");
        formatRp.setGroupingSeparator('.');
        formatRp.setMonetaryDecimalSeparator('.');
        formatRp.setDecimalSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);

        return kursIndonesia.format(value).replace(",", ".");
    }

    public void disableVoucherDiscount() {
        this.voucherDiscount = 0;
        totalPaymentTextView.setText(getStringIdrFormat((double) pricePlain));
    }

    public void setVoucher(String voucherCode, String message) {
        voucherCartHachikoView.setVoucher(voucherCode, message);
    }


    public void renderCheckout(long pricePlain) {
        this.pricePlain = pricePlain;
        long resultPrice = pricePlain - voucherDiscount;
        totalPaymentTextView.setText(getStringIdrFormat((double) resultPrice));
    }

    public int getVoucherViewHeight() {
        return voucherCartHachikoView.getMeasuredHeight();
    }


    public int getCheckoutViewHeight() {
        return checkoutContainer.getHeight();
    }


    public void enableCheckoutButton() {
        checkoutButton.setEnabled(true);
    }

    public void disableCheckoutButton() {
        checkoutButton.setEnabled(false);
    }

    public String getVoucherCode() {
        return voucherCartHachikoView.getVoucherCode();
    }

    public long getDiscountPricePlain() {
        return voucherDiscount;
    }

    public void setTextButton(String checkoutButtonText) {
        checkoutButton.setText(checkoutButtonText);
    }

    public interface ActionListener {

        void onCheckoutButtonClicked();
    }
}
