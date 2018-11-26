package com.tokopedia.digital.nostylecategory.digitalcategory.presentation.compoundview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.digital.R;

/**
 * Created by Rizky on 07/09/18.
 */
public class DigitalBuyNoStyleView extends LinearLayout {

    private TextView textTotalPrice;
    private TextView textBalance;

    public DigitalBuyNoStyleView(Context context) {
        super(context);
        init(context);
    }

    public DigitalBuyNoStyleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DigitalBuyNoStyleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DigitalBuyNoStyleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_mitra_digital_buy, this,
                true);
        textTotalPrice = view.findViewById(R.id.text_total_price);
        textBalance = view.findViewById(R.id.text_balance);
    }

    public void renderBuyView(Product product) {
        textTotalPrice.setText(product.getPrice());
        textBalance.setText("Rp 2.300.000");
    }

}