package com.tokopedia.productcard.v2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
        setLocationMarginLeft();
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

    private void setLocationMarginLeft() {
        int marginLeftPixel =
            isViewVisible(linearLayoutShopBadges)
                    ? getDimensionPixelSize(R.dimen.dp_4)
                    : getDimensionPixelSize(R.dimen.dp_8);

        setMarginsToView(textViewShopLocation, marginLeftPixel, -1, -1, -1);
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
            setViewConstraintTopToBottomOf(labelOffers.getId(), labelCredibility.getId(), R.dimen.dp_8);
        }
        else if(isViewVisible(imageRating)) {
            setViewConstraintTopToBottomOf(labelOffers.getId(), imageRating.getId(), R.dimen.dp_8);
        }
        else if(isViewVisible(textViewReviewCount)) {
            setViewConstraintTopToBottomOf(labelOffers.getId(), textViewReviewCount.getId(), R.dimen.dp_8);
        }
        else {
            setViewConstraintTopToBottomOf(labelOffers.getId(), textViewShopLocation.getId(), R.dimen.dp_8);
        }
    }
}
