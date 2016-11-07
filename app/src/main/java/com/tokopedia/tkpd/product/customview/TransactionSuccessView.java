package com.tokopedia.tkpd.product.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.product.listener.ProductDetailView;
import com.tokopedia.tkpd.product.model.productdetail.ProductDetailData;

import butterknife.Bind;

/**
 * Created by alvarisi on 9/8/16.
 */
public class TransactionSuccessView extends BaseView<ProductDetailData, ProductDetailView> {

    @Bind(R2.id.tv_success)
    TextView tvSuccessRate;

    public TransactionSuccessView(Context context) {
        super(context);
    }

    public TransactionSuccessView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_transaction_success;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(INVISIBLE);
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        switch (data.getStatistic().getProductSuccessRate()) {
            case "0":
                tvSuccessRate.setVisibility(INVISIBLE);
                setVisibility(INVISIBLE);
                break;
            default:
                tvSuccessRate.setVisibility(VISIBLE);
                tvSuccessRate.setText(Html.fromHtml("<b>"
                        + data.getStatistic().getProductSuccessRate()
                        + "%</b> "
                        + getContext().getString(R.string.tx_success_from)
                        + " <b>"
                        + data.getStatistic().getProductTransactionCount()
                        + "</b> "
                        + getContext().getString(R.string.tx_title)));

                setVisibility(VISIBLE);
                break;
        }
    }
}
