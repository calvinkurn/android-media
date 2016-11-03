package com.tokopedia.tkpd.review.presenter;

import android.os.Bundle;

import com.tokopedia.tkpd.inboxreputation.model.param.ActReviewPass;
import com.tokopedia.tkpd.review.model.most_helpful_review.HelpfulReview;
import com.tokopedia.tkpd.review.model.most_helpful_review.MostHelpfulReviewModel;
import com.tokopedia.tkpd.review.model.product_review.AdvanceReview;
import com.tokopedia.tkpd.util.PagingHandler;
import com.tokopedia.tkpd.util.RatingStatsUtils;
import com.tokopedia.tkpd.var.RecyclerViewItem;

import org.json.JSONObject;

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
