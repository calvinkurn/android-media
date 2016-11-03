package com.tokopedia.tkpd.product.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.product.listener.ProductDetailView;
import com.tokopedia.tkpd.product.model.productdetail.ProductDetailData;

import butterknife.Bind;

/**
 * Created by ANGGA on 10/29/2015.
 */
public class ErrorShopView extends BaseView<ProductDetailData, ProductDetailView> {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_sub_title)
    TextView tvSubTitle;

    public ErrorShopView(Context context) {
        super(context);
    }

    public ErrorShopView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_error_product;
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
        switch (data.getShopInfo().getShopStatus()) {
            case 1:
                setVisibility(View.GONE);
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                listener.onProductShopInfoError();
                setVisibility(VISIBLE);
                tvTitle.setText(data.getShopInfo().getShopStatusTitle() != null
                        && !data.getShopInfo().getShopStatusTitle().isEmpty()
                        ? data.getShopInfo().getShopStatusTitle() : "");
                tvSubTitle.setText(data.getShopInfo().getShopStatusMessage() != null
                        && !data.getShopInfo().getShopStatusMessage().isEmpty()
                        ? data.getShopInfo().getShopStatusMessage() : "");
                break;
        }
    }
}
