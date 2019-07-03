package com.tokopedia.productcard.v2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintSet;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.productcard.R;

public class ProductCardViewSmallGrid extends ProductCardView {

    protected ImageView imageShop;

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
    protected void findViews(View inflatedView) {
        super.findViews(inflatedView);

        imageShop = inflatedView.findViewById(R.id.imageShop);
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
        int marginTopDp = isViewVisible(textViewShopName) ? R.dimen.dp_2 : R.dimen.dp_8;

        setViewMarginsInConstraintLayoutProductCard(textViewProductName.getId(), ConstraintSet.TOP, marginTopDp);
    }

    private void setPriceMarginTop() {
        int marginTopDp = isViewVisible(labelDiscount) ? R.dimen.dp_2 : R.dimen.dp_4;

        setViewMarginsInConstraintLayoutProductCard(textViewPrice.getId(), ConstraintSet.TOP, marginTopDp);
    }

    private void setLocationMarginLeft() {
        int marginStartDp = isViewVisible(linearLayoutShopBadges) ? R.dimen.dp_4 : R.dimen.dp_8;

        setViewMarginsInConstraintLayoutProductCard(textViewShopLocation.getId(), ConstraintSet.START, marginStartDp);
    }

    private void setReviewCountMarginLeft() {
        int marginStartDp = isViewVisible(imageRating) ? R.dimen.dp_4 : R.dimen.dp_8;

        setViewMarginsInConstraintLayoutProductCard(textViewReviewCount.getId(), ConstraintSet.START, marginStartDp);
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

    public void setImageShopVisible(boolean isVisible) {
        imageShop.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setImageShopUrl(String imageUrl) {
        ImageHandler.loadImageCircle2(getContext(), imageShop, imageUrl);
    }
}
