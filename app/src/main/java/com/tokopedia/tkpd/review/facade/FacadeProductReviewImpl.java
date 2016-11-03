package com.tokopedia.tkpd.review.facade;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.tkpd.review.model.helpful_review.HelpfulReviewList;
import com.tokopedia.tkpd.review.model.product_review.AdvanceReview;
import com.tokopedia.tkpd.review.model.product_review.ReviewProductModel;
import com.tokopedia.tkpd.util.PagingHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Steven on 12/01/2016.
 */
public interface FacadeProductReviewImpl {

    void getMostHelpfulReview(HashMap<String, String> productId, GetHelpfulReviewListener listener);

    void getReputation(@NonNull Context context, HashMap paramReview, GetReviewListener getReviewListener);

    AdvanceReview getModelRatingStats(JSONObject result);

    List<ReviewProductModel> getListReputation(JSONObject result);

    PagingHandler.PagingHandlerModel getPaging(JSONObject result);

    void storeFirstPage(String product_id, String NAV, JSONObject result);

    void getReputationFromCache(String productID, String NAV, GetReputationCacheListener listener);

    void unsubscribeNetwork();


    interface GetReviewListener {

        void onError(String error);

        void onThrowable(Throwable e);

        void onTimeout();

        void onSuccess(String product_id, String nav, JSONObject result);
    }

    interface GetHelpfulReviewListener {

        void onError(String error);

        void onThrowable(Throwable e);

        void onTimeout();

        void onSuccess(HelpfulReviewList result);
    }

    interface GetReputationCacheListener {
        void onSuccess(JSONObject result);

        void onError(Throwable e);
    }
}
