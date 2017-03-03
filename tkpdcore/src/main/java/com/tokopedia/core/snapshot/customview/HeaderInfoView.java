package com.tokopedia.core.snapshot.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.snapshot.listener.SnapShotFragmentView;
import com.tokopedia.core.util.MethodChecker;

import butterknife.BindView;

/**
 * Created by hangnadi on 3/1/17.
 */

public class HeaderInfoView extends BaseView<ProductDetailData, SnapShotFragmentView> {

    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.tv_price)
    TextView tvPrice;
    @BindView(R2.id.tv_viewed)
    TextView tvViewed;
    @BindView(R2.id.tv_brought)
    TextView tvBrought;
    @BindView(R2.id.label_cashback)
    TextView cashbackTextView;
    @BindView(R2.id.cashback_holder)
    LinearLayout cashbackHolder;
    @BindView(R2.id.title_viewed)
    TextView titleViewed;
    @BindView(R2.id.title_sold)
    TextView titleSold;

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
        return R.layout.view_header_product_info;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(INVISIBLE);
        tvBrought.setVisibility(GONE);
        tvViewed.setVisibility(GONE);
        titleViewed.setVisibility(GONE);
        titleSold.setVisibility(GONE);
        cashbackHolder.setVisibility(GONE);
        tvBrought.setText("");
        tvViewed.setText("");
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        tvName.setText(MethodChecker.fromHtml(data.getInfo().getProductName()));
        tvPrice.setText(data.getInfo().getProductPrice());
        setVisibility(VISIBLE);
    }

    public void renderTempData(ProductPass productPass) {
        tvName.setText(productPass.getProductName());
        tvPrice.setText(productPass.getProductPrice());
        setVisibility(VISIBLE);
    }
}
