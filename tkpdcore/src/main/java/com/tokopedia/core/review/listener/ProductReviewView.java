package com.tokopedia.core.review.listener;

import android.app.Activity;

import com.tokopedia.core.review.model.helpful_review.HelpfulReviewList;
import com.tokopedia.core.review.model.product_review.AdvanceReview;
import com.tokopedia.core.review.model.product_review.ReviewProductModel;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.RatingStatsUtils;

import java.util.List;

/**
 * Created by Steven on 12/01/2016.
 */
public interface ProductReviewView {

    String getProductID();

    void returnHelpfulReview(HelpfulReviewList model);

    void onConnectionResponse(AdvanceReview model, List<ReviewProductModel> list, PagingHandler.PagingHandlerModel pagingHandlerModel);

    void onConnectionTimeOut();

    void onConnectionTimeOut(String error);

    void onStateResponse(List list, int position, AdvanceReview advanceReview, int currentRating, RatingStatsUtils.RatingType currentRatingType, int page, boolean pageHasNext);

    void onCacheResponse(AdvanceReview modelRatingStats, List<ReviewProductModel> listReputation, PagingHandler.PagingHandlerModel paging);

    String getNAV();

    Activity getActivity();

    void showLoadingDialog();

    void dismissLoadingDialog();

    void showSnackbar(String message);

    void showNetworkErrorSnackbar();

    String getShopId();
}
