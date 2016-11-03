package com.tokopedia.tkpd.product.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.product.listener.ProductDetailView;
import com.tokopedia.tkpd.product.model.productdetail.ProductDetailData;

import butterknife.Bind;

/**
 * Created by Angga.Prasetiyo on 02/11/2015.
 */
public class LastUpdateView extends BaseView<ProductDetailData, ProductDetailView> {
    private static final String TAG = LastUpdateView.class.getSimpleName();

    @Bind(R.id.tv_last_update)
    TextView tvLastUpdate;

    public LastUpdateView(Context context) {
        super(context);
    }

    public LastUpdateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_last_update_product;
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
        tvLastUpdate.setText(Html.fromHtml(getContext().getString(R.string.title_last_update_price)
                + " "
                + data.getInfo().getProductLastUpdate()));
        setVisibility(VISIBLE);
    }
}
