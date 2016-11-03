package com.tokopedia.tkpd.product.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.product.listener.ProductDetailView;
import com.tokopedia.tkpd.product.model.productdetail.ProductDetailData;

import butterknife.Bind;

/**
 * Created by Angga.Prasetiyo on 23/10/2015.
 */
public class RatingView extends BaseView<ProductDetailData, ProductDetailView> {
    private static final String TAG = RatingView.class.getSimpleName();

    @Bind(R.id.tv_quality)
    TextView tvQualityRate;
    @Bind(R.id.tv_accuracy)
    TextView tvAccuracyRate;
    @Bind(R.id.iv_quality)
    ImageView ivQualityRate;
    @Bind(R.id.iv_accuracy)
    ImageView ivAccuracyRate;

    public RatingView(Context context) {
        super(context);
    }

    public RatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_rate_product_info;
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
        tvQualityRate.setText(data.getRating().getProductRatingPoint());
        tvAccuracyRate.setText(data.getRating().getProductRateAccuracyPoint());
        ivQualityRate.setImageResource(getRatingDrawable(data.getRating().getProductRatingStarPoint()));
        ivAccuracyRate.setImageResource(getRatingDrawable(data.getRating().getProductAccuracyStarRate()));

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("product_id", String.valueOf(data.getInfo().getProductId()));
                bundle.putString("shop_id", String.valueOf(data.getShopInfo().getShopId()));
                bundle.putString("product_name", data.getInfo().getProductName());
                listener.onProductRatingClicked(bundle);
            }
        });

        setVisibility(VISIBLE);
    }


    private int getRatingDrawable(int param) {
        switch (param) {
            case 0:
                return R.drawable.ic_star_none;
            case 1:
                return R.drawable.ic_star_one;
            case 2:
                return R.drawable.ic_star_two;
            case 3:
                return R.drawable.ic_star_three;
            case 4:
                return R.drawable.ic_star_four;
            case 5:
                return R.drawable.ic_star_five;
            default:
                return R.drawable.ic_star_none;
        }
    }
}
