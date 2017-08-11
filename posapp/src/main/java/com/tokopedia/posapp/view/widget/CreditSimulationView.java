package com.tokopedia.posapp.view.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

/**
 * Created by okasurya on 8/11/17.
 */

public class CreditSimulationView extends BaseView<ProductDetailData, ProductDetailView> {
    private ProductDetailView listener;

    public CreditSimulationView(Context context) {
        super(context);
    }

    public CreditSimulationView(Context context, AttributeSet attrs) {
        super(context, attrs);
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

    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {

    }
}
