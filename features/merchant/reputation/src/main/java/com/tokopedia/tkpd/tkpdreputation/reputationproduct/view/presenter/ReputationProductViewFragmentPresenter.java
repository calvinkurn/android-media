package com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.presenter;

import android.content.Context;

import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review.ReviewProductModel;

import android.support.annotation.NonNull;


/**
 * Created by yoasfs on 13/07/17.
 */

public interface ReputationProductViewFragmentPresenter {

    void getLikeDislike(@NonNull Context context,
                        @NonNull String shopId,
                        @NonNull String reviewId);

    void updateFacade(@NonNull Context context,
                      int reviewId,
                      @NonNull String productId,
                      @NonNull String shopId,
                      int statusLikeDislike,
                      @NonNull final ReviewProductModel model);

    void postReport(@NonNull Context context,
                    @NonNull String reviewId,
                    @NonNull String shopId,
                    @NonNull String reportMessage);

    void deleteComment(@NonNull Context context,
                       @NonNull String reputationId,
                       int reviewId,
                       @NonNull String shopId);
}
