package com.tokopedia.posapp.view.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.Terms;
import com.tokopedia.posapp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import java.util.ArrayList;

/**
 * Created by okasurya on 8/11/17.
 */

public class CreditSimulationView extends BaseView<ProductDetailData, ProductDetailView> {
    public static final String KEY_INSTALLMENT_DATA = "WHOLESALE_DATA";

    private ProductDetailView listener;

    LinearLayout container;
    private TextView textInstallmentPercentage;
    private TextView month3;
    private TextView month6;
    private TextView month12;
    private TextView month18;
    private TextView month24;

    public CreditSimulationView(Context context) {
        super(context);
    }

    public CreditSimulationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        container = findViewById(R.id.container);
        textInstallmentPercentage = findViewById(R.id.text_installment_percentage);
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
        return R.layout.view_credit_simulation;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(INVISIBLE);
    }

    @Override
    public void renderData(@NonNull final ProductDetailData data) {
        textInstallmentPercentage.setText(
                String.format(
                        getResources().getString(R.string.installment_percentage_label),
                        data.getInfo().getInstallmentMinPercentage()
                )
        );

        if (data.getInfo() != null
                && data.getInfo().getProductInstallments() != null
                && data.getInfo().getProductInstallments().get(0) != null
                && data.getInfo().getProductInstallments().get(0).getTerms() != null) {
            setInstallmentDetails(data.getInfo().getProductInstallments().get(0).getTerms());
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

        setVisibility(VISIBLE);

    }

    public void setInstallmentDetails(Terms terms) {
        if(terms.getRule3Months() != null) month3.setText(terms.getRule3Months().getPrice());
        if(terms.getRule6Months() != null) month6.setText(terms.getRule6Months().getPrice());
        if(terms.getRule12Months() != null) month12.setText(terms.getRule12Months().getPrice());
        if(terms.getRule18Months() != null) month18.setText(terms.getRule18Months().getPrice());
        if(terms.getRule24Months() != null) month24.setText(terms.getRule24Months().getPrice());
    }
}
