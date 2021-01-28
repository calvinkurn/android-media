package com.tokopedia.digital.newcart.presentation.compoundview;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tokopedia.digital.R;
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class DigitalCartCheckoutHolderView extends FrameLayout {

    private TickerCheckoutView promoTickerView;
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
                R.layout.view_holder_digital_checkout, this, true
        );

        promoTickerView = view.findViewById(R.id.promo_ticker_view);
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

    public void setPromoTickerActionListener(TickerCheckoutView.ActionListener actionListener) {
        promoTickerView.setActionListener(actionListener);
    }

    public void showPromoTicker() {
        promoTickerView.setVisibility(VISIBLE);
    }

    public void hidePromoTicker() {
        promoTickerView.setVisibility(GONE);
    }

    public void resetPromoTicker() {
        promoTickerView.resetView();
    }

    public void setPromoInfo(String title, String message, TickerCheckoutView.State state) {
        promoTickerView.setTitle(title);
        promoTickerView.setDesc(message);
        promoTickerView.setState(state);
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


    public void renderCheckout(long pricePlain) {
        this.pricePlain = pricePlain;
        long resultPrice = pricePlain - voucherDiscount;
        totalPaymentTextView.setText(getStringIdrFormat((double) resultPrice));
    }

    public int getVoucherViewHeight() {
        return promoTickerView.getMeasuredHeight();
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
