package com.tokopedia.posapp.product.productdetail.view.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.posapp.R;
import com.tokopedia.tkpdpdp.DescriptionActivity;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

/**
 * Created by yoshua on 12/04/18.
 */

public class DescriptionView extends BaseView<ProductDetailData, ProductDetailView> {

    private TextView tvDesc;
    private LinearLayout descriptionContainer;
    private LinearLayout container;

    String description = "";

    public DescriptionView(Context context) {
        super(context);
        initView(context);
    }

    public DescriptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_description_posapp;
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        tvDesc = findViewById(R.id.tv_description);
        descriptionContainer = findViewById(R.id.tv_desc);
        container = findViewById(R.id.ll_wrapper);
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        description = data.getInfo().getProductDescription() == null ? "" :
                data.getInfo().getProductDescription();
        ClickToggle clickToggleDescription = new ClickToggle();
        container.setOnClickListener(clickToggleDescription);
        tvDesc.setOnClickListener(clickToggleDescription);
        tvDesc.setText(description == null
                || description.equals("")
                || description.equals("0")
                ? getResources().getString(com.tokopedia.tkpdpdp.R.string.no_description_pdp) : MethodChecker.fromHtml(description));
        tvDesc.setAutoLinkMask(0);
        Linkify.addLinks(tvDesc, Linkify.WEB_URLS);
        descriptionContainer.setVisibility(VISIBLE);
        setVisibility(VISIBLE);
    }

    private class ClickToggle implements OnClickListener {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(DescriptionActivity.KEY_DESCRIPTION, description);
            listener.onDescriptionClicked(bundle);
            UnifyTracking.eventPDPExpandDescription();
        }
    }
}