package com.tokopedia.productcard;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.topads.sdk.view.ImpressedImageView;
import com.tokopedia.unifycomponents.Label;

public class SearchProductCardView extends BaseCustomView {

    private static final String LIGHT_GREY = "lightGrey";
    private static final String LIGHT_BLUE = "lightBlue";
    private static final String LIGHT_GREEN = "lightGreen";
    private static final String LIGHT_RED = "lightRed";
    private static final String LIGHT_ORANGE = "lightOrange";
    private static final String DARK_GREY = "darkGrey";
    private static final String DARK_BLUE = "darkBlue";
    private static final String DARK_GREEN = "darkGreen";
    private static final String DARK_RED = "darkRed";
    private static final String DARK_ORANGE = "darkOrange";

    protected ConstraintLayout productCardConstraintLayout;
    protected TextView textName;
    protected TextView textPrice;
    protected TextView textDiscount;
    protected TextView textSlashedPrice;
    protected ImpressedImageView imageView;
    protected Label promoLabel;
    protected ImageView topAdsIcon;
    protected ImageView wishlistButton;
    protected ImageView ratingView;
    protected TextView reviewCountView;
    protected Label credibilityLabel;
    protected LinearLayout shopBadgesContainer;
    protected TextView textLocation;
    protected TextView textShopName;
    protected Label offersLabel;
    protected int layout;
    protected boolean fixedHeight = false;

    public SearchProductCardView(@NonNull Context context) {
        super(context);
        init();
    }

    public SearchProductCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchProductCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setFixedHeight(boolean fixedHeight) {
        this.fixedHeight = fixedHeight;
    }

    protected void init() {
        final View view = inflate(getContext(), getLayout(), this);

        textName = view.findViewById(R.id.textName);
        textPrice = view.findViewById(R.id.textPrice);
        textDiscount = view.findViewById(R.id.textDiscount);
        textSlashedPrice = view.findViewById(R.id.textSlashedPrice);
        imageView = view.findViewById(R.id.image);
        promoLabel = view.findViewById(R.id.promoLabel);
        topAdsIcon = view.findViewById(R.id.topAdsIcon);
        wishlistButton = view.findViewById(R.id.btnWishlist);
        ratingView = view.findViewById(R.id.rating);
        reviewCountView = view.findViewById(R.id.reviewCount);
        credibilityLabel = view.findViewById(R.id.credibilityLabel);
        shopBadgesContainer = view.findViewById(R.id.shopBadgesContainer);
        textLocation = view.findViewById(R.id.textLocation);
        textShopName = view.findViewById(R.id.textShopName);
        offersLabel = view.findViewById(R.id.offersLabel);
        productCardConstraintLayout = view.findViewById(R.id.productCardConstraintLayout);

        textName.setLineSpacing(0f, 1f);
    }

    public void setTitle(String title) {
        textName.setText(MethodChecker.fromHtml(title));
    }

    public void setTitleMarginsWithNegativeDefaultValue(int leftPixel, int topPixel, int rightPixel, int bottomPixel) {
        setMarginsToView(textName, leftPixel, topPixel, rightPixel, bottomPixel);
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

    public void setPriceMarginsWithNegativeDefaultValue(int leftPixel, int topPixel, int rightPixel, int bottomPixel) {
        setMarginsToView(textPrice, leftPixel, topPixel, rightPixel, bottomPixel);
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

    public void setWishlistButtonImage(boolean isWishlisted) {
        if(isWishlisted) {
            wishlistButton.setImageResource(R.drawable.ic_wishlist_red_product_card);
        }
        else {
            wishlistButton.setImageResource(R.drawable.ic_wishlist_product_card);
        }
    }

    public void setWishlistButtonOnClickListener(View.OnClickListener onClickListener) {
        wishlistButton.setOnClickListener(onClickListener);
    }

    public void setRatingVisible(boolean isVisible) {
        ratingView.setVisibility(isVisible ? View.VISIBLE : (fixedHeight ? View.INVISIBLE : View.GONE));
    }

    public void setReviewVisible(boolean isVisible) {
        reviewCountView.setVisibility(isVisible ? View.VISIBLE : (fixedHeight ? View.INVISIBLE : View.GONE));
    }

    public void setRating(int rating) {
        ratingView.setImageResource(getRatingDrawable(rating));
    }

    public void setReviewCount(int reviewCount) {
        reviewCountView.setText(getReviewCountFormattedAsText(reviewCount));
    }

    public String getReviewCountFormattedAsText(int reviewCount) {
        return "(" + reviewCount + ")";
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
        return R.layout.search_product_card_layout;
    }

    public void setShopBadgesVisible(boolean isVisible) {
        shopBadgesContainer.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void clearShopBadgesContainer() {
        shopBadgesContainer.removeAllViews();
    }

    public void addShopBadge(View view) {
        shopBadgesContainer.addView(view);
    }

    public void setTextLocationVisible(boolean isVisible) {
        textLocation.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setTextLocation(String location) {
        textLocation.setText(MethodChecker.fromHtml(location));
    }

    public void setTextLocationMarginsWithNegativeDefaultValue(int leftPixel, int topPixel, int rightPixel, int bottomPixel) {
        setMarginsToView(textLocation, leftPixel, topPixel, rightPixel, bottomPixel);
    }

    public void setReviewCountMarginsWithNegativeDefaultValue(int leftPixel, int topPixel, int rightPixel, int bottomPixel) {
        setMarginsToView(reviewCountView, leftPixel, topPixel, rightPixel, bottomPixel);
    }

    private void setMarginsToView(View view, int leftPixel, int topPixel, int rightPixel, int bottomPixel) {
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

    public void setTextShopNameVisible(boolean isVisible) {
        textShopName.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setTextShopName(String shopName) {
        textShopName.setText(MethodChecker.fromHtml(shopName));
    }

    public void setPromoLabelVisible(boolean isVisible) {
        promoLabel.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setPromoLabelText(String promoLabelText) {
        promoLabel.setText(MethodChecker.fromHtml(promoLabelText));
    }

    public void setPromoLabelType(String promoLabelType) {
        promoLabel.setLabelType(getLabelTypeFromString(promoLabelType));
    }

    public void setCredibilityLabelVisible(boolean isVisible) {
        credibilityLabel.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setCredibilityLabelText(String credibilityLabelText) {
        credibilityLabel.setText(MethodChecker.fromHtml(credibilityLabelText));
    }

    public void setCredibilityLabelType(String credibilityLabelType) {
        credibilityLabel.setLabelType(getLabelTypeFromString(credibilityLabelType));
    }

    public void setOffersLabelVisible(boolean isVisible) {
        offersLabel.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setOffersLabelText(String offersLabelText) {
        offersLabel.setText(MethodChecker.fromHtml(offersLabelText));
    }

    public void setOffersLabelType(String offersLabelType) {
        offersLabel.setLabelType(getLabelTypeFromString(offersLabelType));
    }

    private int getLabelTypeFromString(String labelType) {
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

    public void setOffersLabelConstraintTopToBottomOfTextLocation() {
        setViewConstraintTopToBottomOf(offersLabel.getId(), textLocation.getId(), R.dimen.dp_4);
    }

    public void setOffersLabelConstraintTopToBottomOfRatingView() {
        setViewConstraintTopToBottomOf(offersLabel.getId(), ratingView.getId(), R.dimen.dp_4);
    }

    public void setOffersLabelConstraintTopToBottomOfReviewCount() {
        setViewConstraintTopToBottomOf(offersLabel.getId(), reviewCountView.getId(), R.dimen.dp_4);
    }

    public void setOffersLabelConstraintTopToBottomOfCredibilityLabel() {
        setViewConstraintTopToBottomOf(offersLabel.getId(), credibilityLabel.getId(), R.dimen.dp_4);
    }

    private void setViewConstraintTopToBottomOf(int viewLayoutId, int bottomOfLayoutId, @DimenRes int topMarginDp) {
        if (productCardConstraintLayout != null) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(productCardConstraintLayout);

            int topMarginPixel = getContext().getResources().getDimensionPixelSize(topMarginDp);
            constraintSet.connect(viewLayoutId, ConstraintSet.TOP, bottomOfLayoutId, ConstraintSet.BOTTOM, topMarginPixel);

            constraintSet.applyTo(productCardConstraintLayout);
        }
    }
}
