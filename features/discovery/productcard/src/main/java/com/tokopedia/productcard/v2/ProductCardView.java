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
import android.view.ViewGroup;
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

    protected ConstraintLayout constraintLayoutProductCard;
    protected ImpressedImageView imageProduct;
    protected ImageView buttonWishlist;
    protected Label labelPromo;
    protected Typography textViewShopName;
    protected Typography textViewProductName;
    protected Label labelDiscount;
    protected Typography textViewSlashedPrice;
    protected Typography textViewPrice;
    protected LinearLayout linearLayoutShopBadges;
    protected Typography textViewShopLocation;
    protected ImageView imageRating;
    protected Typography textViewReviewCount;
    protected Label labelCredibility;
    protected Label labelOffers;
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
        final View view = inflate(getContext(), getLayout(), this);

        constraintLayoutProductCard = view.findViewById(R.id.constraintLayoutProductCard);
        imageProduct = view.findViewById(R.id.imageProduct);
        buttonWishlist = view.findViewById(R.id.buttonWishlist);
        labelPromo = view.findViewById(R.id.labelPromo);
        textViewShopName = view.findViewById(R.id.textViewShopName);
        textViewProductName = view.findViewById(R.id.textViewProductName);
        labelDiscount = view.findViewById(R.id.labelDiscount);
        textViewSlashedPrice = view.findViewById(R.id.textViewSlashedPrice);
        textViewPrice = view.findViewById(R.id.textViewPrice);
        linearLayoutShopBadges = view.findViewById(R.id.linearLayoutShopBadges);
        textViewShopLocation = view.findViewById(R.id.textViewShopLocation);
        imageRating = view.findViewById(R.id.imageRating);
        textViewReviewCount = view.findViewById(R.id.textViewReviewCount);
        labelCredibility = view.findViewById(R.id.labelCredibility);
        labelOffers = view.findViewById(R.id.labelOffers);
        imageTopAds = view.findViewById(R.id.imageTopAds);


        textViewProductName.setLineSpacing(0f, 1f);
    }

    protected abstract int getLayout();

    public abstract void realignLayout();

    public void setImageProductVisible(boolean isVisible) {
        imageProduct.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setImageProductUrl(String imageUrl) {
        ImageHandler.loadImageFitCenter(getContext(), imageProduct, imageUrl);
    }

    public void setImageProductViewHintListener(ImpressHolder holder, ImpressedImageView.ViewHintListener listener) {
        imageProduct.setViewHintListener(holder, listener);
    }

    public void setButtonWishlistVisible(boolean isVisible) {
        buttonWishlist.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setButtonWishlistImage(boolean isWishlisted) {
        if(isWishlisted) {
            buttonWishlist.setImageResource(R.drawable.product_card_ic_wishlist_red);
        }
        else {
            buttonWishlist.setImageResource(R.drawable.product_card_ic_wishlist);
        }
    }

    public void setButtonWishlistOnClickListener(View.OnClickListener onClickListener) {
        buttonWishlist.setOnClickListener(onClickListener);
    }

    public void setLabelPromoVisible(boolean isVisible) {
        labelPromo.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setLabelPromoText(String promoLabelText) {
        labelPromo.setText(MethodChecker.fromHtml(promoLabelText));
    }

    public void setLabelPromoType(String promoLabelType) {
        labelPromo.setLabelType(getLabelTypeFromString(promoLabelType));
    }

    public void setShopNameVisible(boolean isVisible) {
        textViewShopName.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setShopNameText(String shopName) {
        textViewShopName.setText(MethodChecker.fromHtml(shopName));
    }

    public void setProductNameVisible(boolean isVisible) {
        textViewProductName.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setProductNameText(String title) {
        textViewProductName.setText(MethodChecker.fromHtml(title));
    }

    public void setLabelDiscountVisible(boolean isVisible) {
        labelDiscount.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setLabelDiscountText(int discount) {
        String discountText = Integer.toString(discount) + "%";
        labelDiscount.setText(discountText);
    }

    public void setSlashedPriceVisible(boolean isVisible) {
        textViewSlashedPrice.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setSlashedPriceText(String slashedPrice) {
        textViewSlashedPrice.setText(slashedPrice);
        textViewSlashedPrice.setPaintFlags(
                textViewSlashedPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
        );
    }

    public void setPriceVisible(boolean isVisible) {
        textViewPrice.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setPriceText(String price) {
        textViewPrice.setText(price);
    }

    public void setShopBadgesVisible(boolean isVisible) {
        linearLayoutShopBadges.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void removeAllShopBadges() {
        linearLayoutShopBadges.removeAllViews();
    }

    public void addShopBadge(View view) {
        linearLayoutShopBadges.addView(view);
    }

    public void setShopLocationVisible(boolean isVisible) {
        textViewShopLocation.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setShopLocationText(String location) {
        textViewShopLocation.setText(MethodChecker.fromHtml(location));
    }

    public void setImageRatingVisible(boolean isVisible) {
        imageRating.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setRating(int rating) {
        imageRating.setImageResource(getRatingDrawable(rating));
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
        textViewReviewCount.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setReviewCount(int reviewCount) {
        textViewReviewCount.setText(getReviewCountFormattedAsText(reviewCount));
    }

    public String getReviewCountFormattedAsText(int reviewCount) {
        return "(" + reviewCount + ")";
    }

    public void setLabelCredibilityVisible(boolean isVisible) {
        labelCredibility.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setLabelCredibilityText(String credibilityLabelText) {
        labelCredibility.setText(MethodChecker.fromHtml(credibilityLabelText));
    }

    public void setLabelCredibilityType(String credibilityLabelType) {
        labelCredibility.setLabelType(getLabelTypeFromString(credibilityLabelType));
    }

    public void setLabelOffersVisible(boolean isVisible) {
        labelOffers.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setLabelOffersText(String offersLabelText) {
        labelOffers.setText(MethodChecker.fromHtml(offersLabelText));
    }

    public void setLabelOffersType(String offersLabelType) {
        labelOffers.setLabelType(getLabelTypeFromString(offersLabelType));
    }

    public void setImageTopAdsVisible(boolean isVisible) {
        imageTopAds.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    protected void setMarginsToView(View view, int leftPixel, int topPixel, int rightPixel, int bottomPixel) {
        if(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

            layoutParams.setMargins(
                    leftPixel < 0 ? layoutParams.leftMargin : leftPixel,
                    topPixel < 0 ? layoutParams.topMargin : topPixel,
                    rightPixel < 0 ? layoutParams.rightMargin : rightPixel,
                    bottomPixel < 0 ? layoutParams.bottomMargin : bottomPixel
            );

            view.setLayoutParams(layoutParams);
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

    protected void setViewConstraintInConstraintLayoutProductCard(@IdRes int startLayoutId, int startSide, @IdRes int endLayoutId, int endSide, @DimenRes int marginDp) {
        if (constraintLayoutProductCard != null) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayoutProductCard);

            int marginPixel = getDimensionPixelSize(marginDp);
            constraintSet.connect(startLayoutId, startSide, endLayoutId, endSide, marginPixel);

            constraintSet.applyTo(constraintLayoutProductCard);
        }
    }

    protected boolean isViewVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    protected int getDimensionPixelSize(@DimenRes int id) {
        return getContext().getResources().getDimensionPixelSize(id);
    }
}
