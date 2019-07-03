package com.tokopedia.productcard.v2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintSet;
import android.util.AttributeSet;

import com.tokopedia.productcard.R;

public class ProductCardViewBigGrid extends ProductCardView {

    public ProductCardViewBigGrid(@NonNull Context context) {
        super(context);
    }

    public ProductCardViewBigGrid(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ProductCardViewBigGrid(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayout() {
        return R.layout.product_card_layout_v2_big_grid;
    }

    @Override
    public void realignLayout() {
        setTitleMarginTop();
        setPriceMarginTop();
        setLocationMarginLeft();
        setReviewCountMarginLeft();
        setOffersLabelConstraint();
    }

    private void setTitleMarginTop() {
        if(textViewProductName != null && isViewVisible(textViewProductName)) {
            int marginTopDp = textViewShopName != null && isViewVisible(textViewShopName) ? R.dimen.dp_2 : R.dimen.dp_8;

            setViewMarginsInConstraintLayoutProductCard(textViewProductName.getId(), ConstraintSet.TOP, marginTopDp);
        }
    }

    private void setPriceMarginTop() {
        if (textViewPrice != null && isViewVisible(textViewPrice)) {
            int marginTopDp = labelDiscount != null && isViewVisible(labelDiscount) ? R.dimen.dp_2 : R.dimen.dp_4;

            setViewMarginsInConstraintLayoutProductCard(textViewPrice.getId(), ConstraintSet.TOP, marginTopDp);
        }
    }

    private void setLocationMarginLeft() {
        if (textViewShopLocation != null && isViewVisible(textViewShopLocation)) {
            int marginStartDp = linearLayoutShopBadges != null && isViewVisible(linearLayoutShopBadges) ? R.dimen.dp_4 : R.dimen.dp_8;

            setViewMarginsInConstraintLayoutProductCard(textViewShopLocation.getId(), ConstraintSet.START, marginStartDp);
        }
    }

    private void setReviewCountMarginLeft() {
        if (textViewReviewCount != null && isViewVisible(textViewReviewCount)) {
            int marginStartDp = imageRating != null && isViewVisible(imageRating) ? R.dimen.dp_4 : R.dimen.dp_8;

            setViewMarginsInConstraintLayoutProductCard(textViewReviewCount.getId(), ConstraintSet.START, marginStartDp);
        }
    }

    private void setOffersLabelConstraint() {
        if (labelOffers != null && isViewVisible(labelOffers)) {
            if (labelCredibility != null && isViewVisible(labelCredibility)) {
                setViewConstraintInConstraintLayoutProductCard(
                        labelOffers.getId(), ConstraintSet.TOP, labelCredibility.getId(), ConstraintSet.BOTTOM, R.dimen.dp_8
                );
            } else if (imageRating != null && isViewVisible(imageRating)) {
                setViewConstraintInConstraintLayoutProductCard(
                        labelOffers.getId(), ConstraintSet.TOP, imageRating.getId(), ConstraintSet.BOTTOM, R.dimen.dp_8
                );
            } else if (textViewReviewCount != null && isViewVisible(textViewReviewCount)) {
                setViewConstraintInConstraintLayoutProductCard(
                        labelOffers.getId(), ConstraintSet.TOP, textViewReviewCount.getId(), ConstraintSet.BOTTOM, R.dimen.dp_8
                );
            } else if(textViewShopLocation != null && isViewVisible(textViewShopLocation)) {
                setViewConstraintInConstraintLayoutProductCard(
                        labelOffers.getId(), ConstraintSet.TOP, textViewShopLocation.getId(), ConstraintSet.BOTTOM, R.dimen.dp_8
                );
            }
        }
    }
}
