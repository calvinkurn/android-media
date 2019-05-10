package com.tokopedia.productcard;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.topads.sdk.view.ImpressedImageView;

public class ProductCardViewFixedHeight extends ProductCardView {

    public ProductCardViewFixedHeight(@NonNull Context context) {
        super(context);
        init();
    }

    public ProductCardViewFixedHeight(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProductCardViewFixedHeight(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected int getLayout() {
        return R.layout.product_card_layout_fixed_height;
    }

    public void setDiscount(int discount) {
        if (discount > 0) {
            String discountText = Integer.toString(discount) + "%";
            textDiscount.setText(discountText);
            textDiscount.setVisibility(VISIBLE);
            textSlashedPrice.setVisibility(VISIBLE);
        } else {
            textDiscount.setVisibility(INVISIBLE);
            textSlashedPrice.setVisibility(INVISIBLE);
        }
    }

    public void setRatingReviewCount(int rating, int reviewCount) {
        if (rating > 0 && rating <= 5) {
            ratingReviewContainer.setVisibility(View.VISIBLE);
            ratingView.setImageResource(getRatingDrawable(rating));
            reviewCountView.setText("(" + Integer.toString(reviewCount) + ")");
        } else {
            ratingReviewContainer.setVisibility(View.INVISIBLE);
        }
    }
}
