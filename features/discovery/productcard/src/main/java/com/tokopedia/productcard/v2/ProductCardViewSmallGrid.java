package com.tokopedia.productcard.v2;

import android.content.Context;
import android.drm.DrmStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintSet;
import android.util.AttributeSet;

import com.tokopedia.productcard.R;

public class ProductCardViewSmallGrid extends ProductCardView {

    public ProductCardViewSmallGrid(@NonNull Context context) {
        super(context);
    }

    public ProductCardViewSmallGrid(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ProductCardViewSmallGrid(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayout() {
        return R.layout.product_card_layout_v2_small_grid;
    }

    @Override
    public void realignLayout() {
        setTitleMarginTop();
        setPriceMarginTop();
        setLocationConstraintLeft();
        setReviewCountMarginLeft();
        setOffersLabelConstraint();
    }

    private void setTitleMarginTop() {
        int marginTopPixel =
                isViewVisible(textViewShopName)
                        ? getDimensionPixelSize(R.dimen.dp_2)
                        : getDimensionPixelSize(R.dimen.dp_8);

        setMarginsToView(textViewProductName, -1, marginTopPixel, -1, -1);
    }

    private void setPriceMarginTop() {
        int marginTopPixel =
                isViewVisible(labelDiscount)
                        ? getDimensionPixelSize(R.dimen.dp_2)
                        : getDimensionPixelSize(R.dimen.dp_4);

        setMarginsToView(textViewPrice, -1, marginTopPixel, -1, -1);
    }

//    private void setLocationMarginLeft() {
//        int marginLeftPixel =
//            isViewVisible(linearLayoutShopBadges)
//                    ? getDimensionPixelSize(R.dimen.dp_4)
//                    : getDimensionPixelSize(R.dimen.dp_8);
//
//        setMarginsToView(textViewShopLocation, marginLeftPixel, -1, -1, -1);
//    }

    private void setLocationConstraintLeft() {
        if(!isViewVisible(linearLayoutShopBadges)) {
            setViewConstraintInConstraintLayoutProductCard(textViewShopLocation.getId(), ConstraintSet.LEFT, imageProduct.getId(), ConstraintSet.RIGHT, R.dimen.dp_8);
        }
    }

    private void setReviewCountMarginLeft() {
        int marginLeftPixel =
            isViewVisible(imageRating)
                    ? getDimensionPixelSize(R.dimen.dp_4)
                    : getDimensionPixelSize(R.dimen.dp_8);

        setMarginsToView(textViewReviewCount, marginLeftPixel, -1, -1, -1);
    }

    private void setOffersLabelConstraint() {
        if(isViewVisible(labelCredibility)) {
            setViewConstraintInConstraintLayoutProductCard(labelOffers.getId(), ConstraintSet.TOP, labelCredibility.getId(), ConstraintSet.BOTTOM, R.dimen.dp_8);
        }
        else if(isViewVisible(imageRating)) {
            setViewConstraintInConstraintLayoutProductCard(labelOffers.getId(), ConstraintSet.TOP, imageRating.getId(), ConstraintSet.BOTTOM, R.dimen.dp_8);
        }
        else if(isViewVisible(textViewReviewCount)) {
            setViewConstraintInConstraintLayoutProductCard(labelOffers.getId(), ConstraintSet.TOP, textViewReviewCount.getId(), ConstraintSet.BOTTOM, R.dimen.dp_8);
        }
        else {
            setViewConstraintInConstraintLayoutProductCard(labelOffers.getId(), ConstraintSet.TOP, textViewShopLocation.getId(), ConstraintSet.BOTTOM, R.dimen.dp_8);
        }
    }
}
