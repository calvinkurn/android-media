package com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.presenter;

import android.os.Bundle;

import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.ActReviewPass;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review.AdvanceReview;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.util.RatingStatsUtils;
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
