package com.tokopedia.core.reputationproduct.presenter;

import android.content.Context;

import com.tokopedia.core.review.model.product_review.ReviewProductModel;

import java.util.Map;


/**
 * Created by yoasfs on 13/07/17.
 */

public interface ReputationProductViewFragmentPresenter {


    void getLikeDislike(Context context, Map<String, String> param);

    void updateFacade(Context context, Map<String, String> param, ReviewProductModel model);

    void postReport(Context context, Map<String, String> param);


    void deleteComment(Context context, Map<String, String> param);
}
