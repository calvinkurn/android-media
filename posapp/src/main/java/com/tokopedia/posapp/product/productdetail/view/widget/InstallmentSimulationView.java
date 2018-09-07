package com.tokopedia.posapp.product.productdetail.view.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import java.util.ArrayList;

/**
 * Created by okasurya on 8/11/17.
 */

public class InstallmentSimulationView extends BaseView<ProductDetailData, ProductDetailView> {
    public static final String KEY_INSTALLMENT_DATA = "WHOLESALE_DATA";

    private ProductDetailView listener;

    LinearLayout container;
    private TextView month3;
    private TextView month6;
    private TextView month12;
    private TextView month18;
    private TextView month24;

    public InstallmentSimulationView(Context context) {
        super(context);
    }

    public InstallmentSimulationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        container = findViewById(R.id.container);
        month3 = findViewById(R.id.month3);
        month6 = findViewById(R.id.month6);
        month12 = findViewById(R.id.month12);
        month18 = findViewById(R.id.month18);
        month24 = findViewById(R.id.month24);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_installment_simulation;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull final ProductDetailData data) {
        if (data.getInfo() != null
                && data.getInfo().getProductInstallments() != null
                && data.getInfo().getProductInstallments().get(0) != null
                && data.getInfo().getProductInstallments().get(0).getTerms() != null) {
            setInstallment(data.getInfo().getProductPrice());
        }

        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(KEY_INSTALLMENT_DATA,
                        new ArrayList<>(data.getInfo().getProductInstallments()));
                listener.onInstallmentClicked(bundle);
            }
        });

    }


    public void setInstallment(String formattedPrice) {
        try {
            int price = CurrencyFormatHelper.convertRupiahToInt(formattedPrice);
            month3.setText(CurrencyFormatHelper.toRupiah(price/3));
            month6.setText(CurrencyFormatHelper.toRupiah(price/6));
            month12.setText(CurrencyFormatHelper.toRupiah(price/12));
            month18.setText(CurrencyFormatHelper.toRupiah(price/18));
            month24.setText(CurrencyFormatHelper.toRupiah(price/24));

            setVisibility(VISIBLE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
