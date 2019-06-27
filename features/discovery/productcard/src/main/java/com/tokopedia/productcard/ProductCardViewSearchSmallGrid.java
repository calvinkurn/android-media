package com.tokopedia.productcard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class ProductCardViewSearchSmallGrid extends ProductCardViewSearch {

    public ProductCardViewSearchSmallGrid(@NonNull Context context) {
        super(context);
    }

    public ProductCardViewSearchSmallGrid(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ProductCardViewSearchSmallGrid(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayout() {
        return R.layout.product_card_layout_search_small_grid;
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
                isViewVisible(textShopName)
                        ? getDimensionPixelSize(R.dimen.dp_2)
                        : getDimensionPixelSize(R.dimen.dp_8);

        setMarginsToView(textName, -1, marginTopPixel, -1, -1);
    }


    private void setPriceMarginTop() {
        int marginTopPixel =
                isViewVisible(textShopName)
                        ? getDimensionPixelSize(R.dimen.dp_2)
                        : getDimensionPixelSize(R.dimen.dp_4);

        setMarginsToView(textPrice, -1, marginTopPixel, -1, -1);
    }

    private void setLocationMarginLeft() {
        int marginLeftPixel =
            isViewVisible(shopBadgesContainer)
                    ? getDimensionPixelSize(R.dimen.dp_4)
                    : getDimensionPixelSize(R.dimen.dp_8);

        setMarginsToView(textLocation, marginLeftPixel, -1, -1, -1);
    }

    private void setReviewCountMarginLeft() {
        int marginLeftPixel =
            isViewVisible(reviewCountView)
                    ? getDimensionPixelSize(R.dimen.dp_4)
                    : getDimensionPixelSize(R.dimen.dp_8);

        setMarginsToView(reviewCountView, marginLeftPixel, -1, -1, -1);
    }

    private void setOffersLabelConstraint() {
        if(isViewVisible(credibilityLabel)) {
            setViewConstraintTopToBottomOf(offersLabel.getId(), credibilityLabel.getId(), R.dimen.dp_4);
        }
        else if(isViewVisible(ratingView)) {
            setViewConstraintTopToBottomOf(offersLabel.getId(), ratingView.getId(), R.dimen.dp_4);
        }
        else if(isViewVisible(reviewCountView)) {
            setViewConstraintTopToBottomOf(offersLabel.getId(), reviewCountView.getId(), R.dimen.dp_4);
        }
        else {
            setViewConstraintTopToBottomOf(offersLabel.getId(), textLocation.getId(), R.dimen.dp_4);
        }
    }
}
