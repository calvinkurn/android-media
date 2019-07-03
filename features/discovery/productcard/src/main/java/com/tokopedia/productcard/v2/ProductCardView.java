package com.tokopedia.productcard.v2;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.DimenRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.productcard.R;
import com.tokopedia.topads.sdk.domain.model.ImpressHolder;
import com.tokopedia.topads.sdk.view.ImpressedImageView;
import com.tokopedia.unifycomponents.Label;
import com.tokopedia.unifyprinciples.Typography;

public abstract class ProductCardView extends BaseCustomView {

    protected static final String LIGHT_GREY = "lightGrey";
    protected static final String LIGHT_BLUE = "lightBlue";
    protected static final String LIGHT_GREEN = "lightGreen";
    protected static final String LIGHT_RED = "lightRed";
    protected static final String LIGHT_ORANGE = "lightOrange";
    protected static final String DARK_GREY = "darkGrey";
    protected static final String DARK_BLUE = "darkBlue";
    protected static final String DARK_GREEN = "darkGreen";
    protected static final String DARK_RED = "darkRed";
    protected static final String DARK_ORANGE = "darkOrange";

    @Nullable
    protected ConstraintLayout constraintLayoutProductCard;
    @Nullable
    protected ImpressedImageView imageProduct;
    @Nullable
    protected ImageView buttonWishlist;
    @Nullable
    protected Label labelPromo;
    @Nullable
    protected Typography textViewShopName;
    @Nullable
    protected Typography textViewProductName;
    @Nullable
    protected Label labelDiscount;
    @Nullable
    protected Typography textViewSlashedPrice;
    @Nullable
    protected Typography textViewPrice;
    @Nullable
    protected LinearLayout linearLayoutShopBadges;
    @Nullable
    protected Typography textViewShopLocation;
    @Nullable
    protected ImageView imageRating;
    @Nullable
    protected Typography textViewReviewCount;
    @Nullable
    protected Label labelCredibility;
    @Nullable
    protected Label labelOffers;
    @Nullable
    protected ImageView imageTopAds;

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

    protected void init() {
        final View inflatedView = inflate(getContext(), getLayout(), this);

        findViews(inflatedView);

        postInit();
    }

    protected void findViews(View inflatedView) {
        constraintLayoutProductCard = inflatedView.findViewById(R.id.constraintLayoutProductCard);
        imageProduct = inflatedView.findViewById(R.id.imageProduct);
        buttonWishlist = inflatedView.findViewById(R.id.buttonWishlist);
        labelPromo = inflatedView.findViewById(R.id.labelPromo);
        textViewShopName = inflatedView.findViewById(R.id.textViewShopName);
        textViewProductName = inflatedView.findViewById(R.id.textViewProductName);
        labelDiscount = inflatedView.findViewById(R.id.labelDiscount);
        textViewSlashedPrice = inflatedView.findViewById(R.id.textViewSlashedPrice);
        textViewPrice = inflatedView.findViewById(R.id.textViewPrice);
        linearLayoutShopBadges = inflatedView.findViewById(R.id.linearLayoutShopBadges);
        textViewShopLocation = inflatedView.findViewById(R.id.textViewShopLocation);
        imageRating = inflatedView.findViewById(R.id.imageRating);
        textViewReviewCount = inflatedView.findViewById(R.id.textViewReviewCount);
        labelCredibility = inflatedView.findViewById(R.id.labelCredibility);
        labelOffers = inflatedView.findViewById(R.id.labelOffers);
        imageTopAds = inflatedView.findViewById(R.id.imageTopAds);
    }

    protected void postInit() {
        if(textViewProductName != null) {
            textViewProductName.setLineSpacing(0f, 1f);
        }
    }

    protected abstract int getLayout();

    public abstract void realignLayout();

    public void setImageProductVisible(boolean isVisible) {
        if(imageProduct != null) {
            imageProduct.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setImageProductUrl(String imageUrl) {
        if (imageProduct != null) {
            ImageHandler.loadImageThumbs(getContext(), imageProduct, imageUrl);
        }
    }

    public void setImageProductViewHintListener(ImpressHolder holder, ImpressedImageView.ViewHintListener listener) {
        if (imageProduct != null) {
            imageProduct.setViewHintListener(holder, listener);
        }
    }

    public void setButtonWishlistVisible(boolean isVisible) {
        if (buttonWishlist != null) {
            buttonWishlist.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setButtonWishlistImage(boolean isWishlisted) {
        if (buttonWishlist != null) {
            if (isWishlisted) {
                buttonWishlist.setImageResource(R.drawable.product_card_ic_wishlist_red);
            } else {
                buttonWishlist.setImageResource(R.drawable.product_card_ic_wishlist);
            }
        }
    }

    public void setButtonWishlistOnClickListener(View.OnClickListener onClickListener) {
        if (buttonWishlist != null) {
            buttonWishlist.setOnClickListener(onClickListener);
        }
    }

    public void setLabelPromoVisible(boolean isVisible) {
        if (labelPromo != null) {
            labelPromo.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setLabelPromoText(String promoLabelText) {
        if (labelPromo != null) {
            labelPromo.setText(MethodChecker.fromHtml(promoLabelText));
        }
    }

    public void setLabelPromoType(String promoLabelType) {
        if (labelPromo != null) {
            labelPromo.setLabelType(getLabelTypeFromString(promoLabelType));
        }
    }

    protected int getLabelTypeFromString(String labelType) {
        switch(labelType) {
            case LIGHT_GREY :
                return Label.GENERAL_LIGHT_GREY;
            case LIGHT_BLUE :
                return Label.GENERAL_LIGHT_BLUE;
            case LIGHT_GREEN :
                return Label.GENERAL_LIGHT_GREEN;
            case LIGHT_RED:
                return Label.GENERAL_LIGHT_RED;
            case LIGHT_ORANGE:
                return Label.GENERAL_LIGHT_ORANGE;
            case DARK_GREY :
                return Label.GENERAL_DARK_GREY;
            case DARK_BLUE :
                return Label.GENERAL_DARK_BLUE;
            case DARK_GREEN :
                return Label.GENERAL_DARK_GREEN;
            case DARK_RED:
                return Label.GENERAL_DARK_RED;
            case DARK_ORANGE:
                return Label.GENERAL_DARK_ORANGE;
            default :
                return Label.GENERAL_LIGHT_GREY;
        }
    }

    public void setShopNameVisible(boolean isVisible) {
        if (textViewShopName != null) {
            textViewShopName.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setShopNameText(String shopName) {
        if (textViewShopName != null) {
            textViewShopName.setText(MethodChecker.fromHtml(shopName));
        }
    }

    public void setProductNameVisible(boolean isVisible) {
        if (textViewProductName != null) {
            textViewProductName.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setProductNameText(String title) {
        if (textViewProductName != null) {
            textViewProductName.setText(MethodChecker.fromHtml(title));
        }
    }

    public void setLabelDiscountVisible(boolean isVisible) {
        if (labelDiscount != null) {
            labelDiscount.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setLabelDiscountText(int discount) {
        if (labelDiscount != null) {
            String discountText = Integer.toString(discount) + "%";
            labelDiscount.setText(discountText);
        }
    }

    public void setSlashedPriceVisible(boolean isVisible) {
        if (textViewSlashedPrice != null) {
            textViewSlashedPrice.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setSlashedPriceText(String slashedPrice) {
        if (textViewSlashedPrice != null) {
            textViewSlashedPrice.setText(slashedPrice);
            textViewSlashedPrice.setPaintFlags(
                    textViewSlashedPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );
        }
    }

    public void setPriceVisible(boolean isVisible) {
        if (textViewPrice != null) {
            textViewPrice.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setPriceText(String price) {
        if (textViewPrice != null) {
            textViewPrice.setText(price);
        }
    }

    public void setShopBadgesVisible(boolean isVisible) {
        if (linearLayoutShopBadges != null) {
            linearLayoutShopBadges.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void removeAllShopBadges() {
        if (linearLayoutShopBadges != null) {
            linearLayoutShopBadges.removeAllViews();
        }
    }

    public void addShopBadge(View view) {
        if (linearLayoutShopBadges != null) {
            linearLayoutShopBadges.addView(view);
        }
    }

    public void setShopLocationVisible(boolean isVisible) {
        if (textViewShopLocation != null) {
            textViewShopLocation.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setShopLocationText(String location) {
        if (textViewShopLocation != null) {
            textViewShopLocation.setText(MethodChecker.fromHtml(location));
        }
    }

    public void setImageRatingVisible(boolean isVisible) {
        if (imageRating != null) {
            imageRating.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setRating(int rating) {
        if (imageRating != null) {
            imageRating.setImageResource(getRatingDrawable(rating));
        }
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

    public void setReviewCountVisible(boolean isVisible) {
        if (textViewReviewCount != null) {
            textViewReviewCount.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setReviewCount(int reviewCount) {
        if (textViewReviewCount != null) {
            textViewReviewCount.setText(getReviewCountFormattedAsText(reviewCount));
        }
    }

    public String getReviewCountFormattedAsText(int reviewCount) {
        return "(" + reviewCount + ")";
    }

    public void setLabelCredibilityVisible(boolean isVisible) {
        if (labelCredibility != null) {
            labelCredibility.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setLabelCredibilityText(String credibilityLabelText) {
        if (labelCredibility != null) {
            labelCredibility.setText(MethodChecker.fromHtml(credibilityLabelText));
        }
    }

    public void setLabelCredibilityType(String credibilityLabelType) {
        if (labelCredibility != null) {
            labelCredibility.setLabelType(getLabelTypeFromString(credibilityLabelType));
        }
    }

    public void setLabelOffersVisible(boolean isVisible) {
        if (labelOffers != null) {
            labelOffers.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setLabelOffersText(String offersLabelText) {
        if (labelOffers != null) {
            labelOffers.setText(MethodChecker.fromHtml(offersLabelText));
        }
    }

    public void setLabelOffersType(String offersLabelType) {
        if (labelOffers != null) {
            labelOffers.setLabelType(getLabelTypeFromString(offersLabelType));
        }
    }

    public void setImageTopAdsVisible(boolean isVisible) {
        if (imageTopAds != null) {
            imageTopAds.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    protected void setViewMarginsInConstraintLayoutProductCard(@IdRes int viewId, int anchor, int marginDp) {
        if (constraintLayoutProductCard != null) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayoutProductCard);

            int marginPixel = getDimensionPixelSize(marginDp);
            constraintSet.setMargin(viewId, anchor, marginPixel);

            constraintSet.applyTo(constraintLayoutProductCard);
        }
    }

    protected void setViewConstraintInConstraintLayoutProductCard(@IdRes int startLayoutId, int startSide, @IdRes int endLayoutId, int endSide, @DimenRes int marginDp) {
        if (constraintLayoutProductCard != null) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayoutProductCard);

            int marginPixel = getDimensionPixelSize(marginDp);
            constraintSet.connect(startLayoutId, startSide, endLayoutId, endSide, marginPixel);

            constraintSet.applyTo(constraintLayoutProductCard);
        }
    }

    protected boolean isViewNotNullAndVisible(@Nullable View view) {
        return view != null && isViewVisible(view);
    }

    protected boolean isViewVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    protected int getDimensionPixelSize(@DimenRes int id) {
        return getContext().getResources().getDimensionPixelSize(id);
    }
}