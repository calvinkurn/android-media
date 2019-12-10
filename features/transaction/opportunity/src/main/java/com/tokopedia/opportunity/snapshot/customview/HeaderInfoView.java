package com.tokopedia.opportunity.snapshot.customview;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.opportunity.R;
import com.tokopedia.opportunity.snapshot.listener.SnapShotFragmentView;
import com.tokopedia.core.util.MethodChecker;

/**
 * Created by hangnadi on 3/1/17.
 */

public class HeaderInfoView extends BaseView<ProductDetailData, SnapShotFragmentView> {

    TextView tvName;
    TextView tvPrice;
    TextView cashbackTextView;
    LinearLayout cashbackHolder;

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutView(), this, true);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        cashbackTextView = (TextView) findViewById(R.id.label_cashback);
        cashbackHolder = (LinearLayout) findViewById(R.id.cashback_holder);
    }

    public HeaderInfoView(Context context) {
        super(context);
    }

    public HeaderInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(SnapShotFragmentView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_header_product_snapshot;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(INVISIBLE);
        cashbackHolder.setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        tvName.setText(MethodChecker.fromHtml(data.getInfo().getProductName()));
        String price = data.getInfo().getProductPrice();
        if (CurrencyFormatHelper.countPrefixCurrency(price) < 1){
            price = getResources().getString(R.string.format_rupiah_with_prefix, price);
        }
        tvPrice.setText(price);
        setVisibility(VISIBLE);
    }

    public void renderTempData(ProductPass productPass) {
        tvName.setText(productPass.getProductName());
        tvPrice.setText(productPass.getProductPrice());
        setVisibility(VISIBLE);
    }
}
