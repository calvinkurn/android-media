package com.tokopedia.productcard;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.productcard.R;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.topads.sdk.view.ImpressedImageView;

public class ProductCardView extends BaseCustomView {

    private TextView textName;
    private TextView textPrice;
    private TextView textDiscount;
    private TextView textSlashedPrice;
    private ImpressedImageView imageView;
    private View topAdsIcon;
    private View wishlistButton;
    private View ratingReviewContainer;
    private ImageView ratingView;
    private TextView reviewCountView;

    public ProductCardView(@NonNull Context context) {
        super(context);
        init();
    }

    public ProductCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProductCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        final View view = inflate(getContext(), R.layout.product_card_layout, this);
        textName = view.findViewById(R.id.textName);
        textPrice = view.findViewById(R.id.textPrice);
        textDiscount = view.findViewById(R.id.textDiscount);
        textSlashedPrice = view.findViewById(R.id.textSlashedPrice);
        imageView = view.findViewById(R.id.image);
        topAdsIcon = view.findViewById(R.id.topAdsIcon);
        wishlistButton = view.findViewById(R.id.btn_wishlist);
        ratingReviewContainer = view.findViewById(R.id.rating_review_container);
        ratingView = view.findViewById(R.id.rating);
        reviewCountView = view.findViewById(R.id.review_count);
    }

    public void setTitle(String title) {
        textName.setText(MethodChecker.fromHtml(title));
    }

    public void setDiscount(int discount) {
        if (discount > 0) {
            String discountText = Integer.toString(discount) + "%";
            textDiscount.setText(discountText);
            textDiscount.setVisibility(VISIBLE);
            textSlashedPrice.setVisibility(VISIBLE);
        } else {
            textDiscount.setVisibility(GONE);
            textSlashedPrice.setVisibility(GONE);
        }
    }

    public void setSlashedPrice(String slashedPrice) {
        textSlashedPrice.setText(slashedPrice);
        textSlashedPrice.setPaintFlags(
                textSlashedPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
        );
    }

    public void setPrice(String price) {
        textPrice.setText(price);
    }

    public void setImageUrl(String imageUrl) {
        ImageHandler.loadImageFitCenter(getContext(), imageView, imageUrl);
    }

    public void setTopAdsVisible(boolean isVisible) {
        topAdsIcon.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setWishlistButtonVisible(boolean isVisible) {
        wishlistButton.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setRatingReviewCount(int rating, int reviewCount) {
        if (rating > 0 && rating <= 5) {
            ratingReviewContainer.setVisibility(View.VISIBLE);
            ratingView.setImageResource(getRatingDrawable(rating));
            reviewCountView.setText("(" + Integer.toString(reviewCount) + ")");
        } else {
            ratingReviewContainer.setVisibility(View.GONE);
        }
    }

    public ImpressedImageView getImageView() {
        return imageView;
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
