package com.tokopedia.productcard;

import android.content.Context;
import android.content.res.TypedArray;
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

public class ProductCardView extends BaseCustomView {
    protected TextView textName;
    protected TextView textPrice;
    protected TextView textDiscount;
    protected TextView textSlashedPrice;
    protected ImpressedImageView imageView;
    protected View topAdsIcon;
    protected View wishlistButton;
    protected ImageView ratingView;
    protected TextView reviewCountView;
    protected int layout;
    protected boolean fixedHeight = false;
    protected ImageView badgeView;
    protected TextView textLocation;

    public ProductCardView(@NonNull Context context) {
        super(context);
        init(null);
    }

    public ProductCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ProductCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void setFixedHeight(boolean fixedHeight) {
        this.fixedHeight = fixedHeight;
    }

    protected void init(@Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(
                    attrs,
                    R.styleable.ProductCardView,
                    0, 0);

            try {
                fixedHeight = a.getBoolean(R.styleable.ProductCardView_fixedHeight, false);
            } finally {
                a.recycle();
            }
        }

        final View view = inflate(getContext(), getLayout(), this);
        textName = view.findViewById(R.id.textName);
        textPrice = view.findViewById(R.id.textPrice);
        textDiscount = view.findViewById(R.id.textDiscount);
        textSlashedPrice = view.findViewById(R.id.textSlashedPrice);
        imageView = view.findViewById(R.id.image);
        topAdsIcon = view.findViewById(R.id.topAdsIcon);
        wishlistButton = view.findViewById(R.id.btn_wishlist);
        ratingView = view.findViewById(R.id.rating);
        reviewCountView = view.findViewById(R.id.review_count);
        badgeView = view.findViewById(R.id.badge);
        textLocation = view.findViewById(R.id.location);
    }

    public void setTitle(String title) {
        textName.setText(MethodChecker.fromHtml(title));
    }

    public void setDiscount(int discount) {
        if (discount > 0) {
            String discountText = Integer.toString(discount) + "%";
            textDiscount.setText(discountText);
            textDiscount.setVisibility(View.VISIBLE);
            textSlashedPrice.setVisibility(View.VISIBLE);
        } else {
            if(fixedHeight) {
                textDiscount.setVisibility(View.INVISIBLE);
                textSlashedPrice.setVisibility(View.INVISIBLE);
            } else {
                textDiscount.setVisibility(View.GONE);
                textSlashedPrice.setVisibility(View.GONE);
            }
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
        //wishlist not implemented yet, don't delete this.
//        wishlistButton.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setRatingReviewCount(int rating, int reviewCount) {
        if (rating > 0 && rating <= 5) {
            ratingView.setImageResource(getRatingDrawable(rating));
            reviewCountView.setText(String.format(getContext().getString(R.string.review_count_format), reviewCount));
        } else {
            if(fixedHeight) {
                ratingView.setVisibility(View.INVISIBLE);
                reviewCountView.setVisibility(View.INVISIBLE);
            } else {

                ratingView.setVisibility(View.GONE);
                reviewCountView.setVisibility(View.GONE);
            }
        }
    }

    public void setBadgeUrl(String url){
        ImageHandler.loadImageFitCenter(getContext(), badgeView, url);
    }

    public void setLocation(String location){
        textLocation.setText(location);
    }

    public ImpressedImageView getImageView() {
        return imageView;
    }

    protected int getRatingDrawable(int param) {
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

    protected int getLayout() {
        if(fixedHeight) {
            return R.layout.product_card_layout_fixed_height;
        } else {
            return R.layout.product_card_layout;
        }
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }
}
