package com.tokopedia.digital.cart.presentation.compoundview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.digital.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * @author anggaprasetiyo on 2/22/17.
 */

public class CheckoutHolderView extends RelativeLayout {

    private TextView tvPrice;
    private TextView tvDiscount;
    private LinearLayout holderVoucherDiscount;
    private TextView tvSubTotalPrice;
    private TextView btnNext;

    private long pricePlain = 0;
    private long voucherDiscount = 0;

    public CheckoutHolderView(Context context) {
        super(context);
        initView(context);
    }

    public CheckoutHolderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CheckoutHolderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(
                R.layout.view_holder_checkout_digital_module, this, true
        );

        tvPrice = findViewById(R.id.tv_value_price);
        tvDiscount = findViewById(R.id.tv_value_discount);
        holderVoucherDiscount = findViewById(R.id.holder_voucher_discount);
        tvSubTotalPrice = findViewById(R.id.tv_value_sub_total);
        btnNext = findViewById(R.id.btn_next);
    }

    public void renderData(
            final IAction actionListener, String price, String totalPrice, long pricePlain
    ) {
        this.pricePlain = pricePlain;
        tvPrice.setText(price);
        tvSubTotalPrice.setText(totalPrice);
        btnNext.setOnClickListener(v -> actionListener.onClickButtonNext());
    }

    public void disableVoucherDiscount() {
        voucherDiscount = 0;
        tvDiscount.setText(getStringIdrFormat((double) this.voucherDiscount));
        holderVoucherDiscount.setVisibility(GONE);
        long totalPrice = pricePlain - voucherDiscount;
        tvSubTotalPrice.setText(getStringIdrFormat((double) totalPrice));
    }

    public void enableVoucherDiscount(long voucherDiscount) {
        this.holderVoucherDiscount.setVisibility(VISIBLE);
        this.voucherDiscount = voucherDiscount;
        tvDiscount.setText(getStringIdrFormat((double) this.voucherDiscount));
        long totalPrice = pricePlain - voucherDiscount;
        tvSubTotalPrice.setText(getStringIdrFormat((double) totalPrice));
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

    public void enableCheckoutButton() {
        btnNext.setEnabled(true);
    }

    public void disableCheckoutButton() {
        btnNext.setEnabled(false);
    }

    public interface IAction {
        void onClickButtonNext();
    }

}
