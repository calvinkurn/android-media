package com.tokopedia.core.product.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.product.listener.ProductDetailView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;

import butterknife.BindView;

/**
 * Created by Angga.Prasetiyo on 26/10/2015.
 */
public class ErrorProductView extends BaseView<ProductDetailData, ProductDetailView> {
    private static final String TAG = ErrorProductView.class.getSimpleName();

    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.tv_sub_title)
    TextView tvSubTitle;

    public ErrorProductView(Context context) {
        super(context);
    }

    public ErrorProductView(Context context, AttributeSet attrs) {
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
        setVisibility(View.GONE);
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        if (data.getInfo().getProductStatus().equals("-1")) {
            if (data.getInfo().getProductStatusMessage() != null && data.getInfo().getProductStatusTitle() != null) {
                tvTitle.setText(data.getInfo().getProductStatusTitle());
                tvSubTitle.setText(data.getInfo().getProductStatusMessage());
                setVisibility(VISIBLE);
            }
            listener.onProductStatusError();
        }
         else if (data.getInfo().getProductStatus().equals("3") &
                data.getShopInfo().getShopStatus() == 1) {
            setVisibility(VISIBLE);
            tvTitle.setText(getContext().getString(R.string.message_prod_depository));
            tvSubTitle.setText(getContext().getString(R.string.message_prod_depository_2));
        } else if (!data.getInfo().getProductStatus().equals("1")) {
            listener.onProductStatusError();
        } else {
            setVisibility(GONE);
        }
    }
}
