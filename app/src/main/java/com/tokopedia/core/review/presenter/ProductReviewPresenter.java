package com.tokopedia.core.review.presenter;

import android.os.Bundle;

import com.tokopedia.core.inboxreputation.model.param.ActReviewPass;
import com.tokopedia.core.review.model.product_review.AdvanceReview;
import com.tokopedia.core.util.RatingStatsUtils;
import com.tokopedia.core.var.RecyclerViewItem;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Steven on 12/01/2016.
 */
public interface ProductReviewPresenter {

    void getMostHelpfulReview();

    void getReputation(HashMap<String,String> paramFacade);

    void saveStateList(Bundle outState, List<RecyclerViewItem> recyclerList
            , int position, AdvanceReview advanceReview, int currentRating, RatingStatsUtils.RatingType currentRatingType, int page, boolean pageHasNext);

    void restoreStateList(Bundle savedInstanceState);

    void getReputationFromCache(String productID, String NAV);

    void unsubscribeNetwork();

    void reportReview(ActReviewPass pass);

    void onDestroyView();
}
