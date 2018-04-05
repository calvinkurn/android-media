package com.tokopedia.tkpd.tkpdreputation.review.shop.view;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.analytic.ReputationTrackingConstant;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductModelContent;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter.ReviewShopInfoTypeFactoryAdapter;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter.ReviewShopModelContent;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter.ReviewShopSeeMoreModelContent;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter.ReviewShopSeeMoreViewHolder;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter.ReviewShopTypeFactoryAdapter;

import java.util.List;

/**
 * Created by normansyahputa on 2/14/18.
 */

public class ReviewShopInfoFragment extends ReviewShopFragment implements ReviewShopSeeMoreViewHolder.ShopReviewSeeMoreHolderListener {

    public static ReviewShopInfoFragment createInstance(String shopId, String shopDomain) {
        ReviewShopInfoFragment shopReviewFragment = new ReviewShopInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SHOP_ID, shopId);
        bundle.putString(SHOP_DOMAIN, shopDomain);
        shopReviewFragment.setArguments(bundle);
        return shopReviewFragment;
    }

    @Override
    protected ReviewShopTypeFactoryAdapter getAdapterTypeFactory() {
        return new ReviewShopInfoTypeFactoryAdapter(this, this, this);
    }

    @Override
    public void onGoToMoreReview() {
        reputationTracking.eventClickSeeMoreReview(getString(R.string.review), shopId, shopReviewPresenter.isMyShop(shopId));
        startActivity(ReviewShopInfoActivity.createIntent(getActivity(), shopId, shopDomain));
    }

    @Override
    protected boolean isLoadMoreEnabledByDefault() {
        return false;
    }


    @Override
    public void renderList(@NonNull List<ReviewShopModelContent> list, boolean b) {
        boolean isSeeMoreEnabled = false;
        if(!list.isEmpty() && list.size() >= 10 && b){
            // add see more
            isSeeMoreEnabled = true;

            list = list.subList(0,10);
        }
        super.renderList(list, b);
        if(isSeeMoreEnabled) {
            getAdapter().addElement(new ReviewShopSeeMoreModelContent());
        }
    }

    @Override
    protected void onLikeDislikeTracking(String productId, boolean status, int adapterPosition) {
        reputationTracking.eventClickLikeDislikeReview(getString(R.string.review), status, adapterPosition, shopId,
                shopReviewPresenter.isMyShop(shopId));
    }

    @Override
    protected void onGoToDetailProductTracking(String productId, int adapterPosition) {
        reputationTracking.eventClickProductPictureOrName(getString(R.string.review), adapterPosition, productId,
                shopReviewPresenter.isMyShop(shopId));
    }

    @Override
    protected void onGoToProfileTracking(int adapterPosition) {
        reputationTracking.eventClickUserAccount(getString(R.string.review), adapterPosition, shopId, shopReviewPresenter.isMyShop(shopId));
    }

    @Override
    public void onMenuClicked(int adapterPosition) {
        reputationTracking.eventCLickThreeDotMenu(getString(R.string.review), adapterPosition, shopId,shopReviewPresenter.isMyShop(shopId));
    }

    @Override
    protected void onDeleteReviewResponseTracking(ReviewProductModelContent element, int adapterPosition) {
        reputationTracking.eventClickChooseThreeDotMenu(getString(R.string.review), adapterPosition, ReputationTrackingConstant.DELETE, shopId
                ,shopReviewPresenter.isMyShop(shopId));
    }

    @Override
    protected void onGoToReportReviewTracking(String shopId, int adapterPosition) {
        reputationTracking.eventClickChooseThreeDotMenu(getString(R.string.review), adapterPosition, ReputationTrackingConstant.REPORT, shopId
                ,shopReviewPresenter.isMyShop(shopId));
    }

    @Override
    public void onSeeReplied(int adapterPosition) {
        reputationTracking.eventClickSeeReplies(getString(R.string.review), adapterPosition, shopId,shopReviewPresenter.isMyShop(shopId));
    }
}
