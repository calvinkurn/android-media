package com.tokopedia.core.reputationproduct.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.inboxreputation.interactor.ActReputationRetrofitInteractor;
import com.tokopedia.core.inboxreputation.interactor.InboxReputationRetrofitInteractor;
import com.tokopedia.core.inboxreputation.model.actresult.ActResult;
import com.tokopedia.core.reputationproduct.fragment.ReputationProductFragmentView;
import com.tokopedia.core.reputationproduct.model.LikeDislike;
import com.tokopedia.core.review.model.product_review.ReviewProductModel;

import java.util.Map;

/**
 * Created by yoasfs on 13/07/17.
 */

public class ReputationProductViewFragmentPresenterImpl implements ReputationProductViewFragmentPresenter {

    private ReputationProductFragmentView reputationProductFragmentView;
    private ActReputationRetrofitInteractor actNetworkInteractor;
    private InboxReputationRetrofitInteractor networkInteractor;
    public ReputationProductViewFragmentPresenterImpl(ReputationProductFragmentView reputationProductFragmentView,
                                                      ActReputationRetrofitInteractor actNetworkInteractor,
                                                      InboxReputationRetrofitInteractor networkInteractor) {

        this.reputationProductFragmentView = reputationProductFragmentView;
        this.actNetworkInteractor = actNetworkInteractor;
        this.networkInteractor = networkInteractor;
    }

    @Override
    public void getLikeDislike(@NonNull Context context,
                               @NonNull String shopId,
                               @NonNull String reviewId) {
        networkInteractor.getLikeDislikeReview(context,
                networkInteractor.getLikeDislikeParam(shopId,
                        reviewId),
                new InboxReputationRetrofitInteractor.LikeDislikeListener() {
            @Override
            public void onSuccess(@NonNull LikeDislike result) {
                reputationProductFragmentView.setResultToModel(result);
            }

            @Override
            public void onTimeout() {

            }

            @Override
            public void onFailAuth() {

            }

            @Override
            public void onThrowable(Throwable e) {

            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onNullData() {

            }

            @Override
            public void onNoConnection() {

            }
        });
    }

    @Override
    public void updateFacade(@NonNull Context context,
                             int reviewId,
                             @NonNull String productId,
                             @NonNull String shopId,
                             int statusLikeDislike,
                             @NonNull final ReviewProductModel model) {
        actNetworkInteractor.likeDislikeReview(context,
                actNetworkInteractor.getActionLikeDislikeParam(String.valueOf(reviewId),
                        productId,
                        shopId,
                        String.valueOf(statusLikeDislike)),
                new ActReputationRetrofitInteractor.ActReputationListener() {
            @Override
            public void onSuccess(ActResult result) {
                reputationProductFragmentView.onSuccessGetLikeDislikeReview();
            }

            @Override
            public void onTimeout() {
                reputationProductFragmentView.onErrorConnectionGetLikeDislikeReview(model);
            }

            @Override
            public void onFailAuth() {

            }

            @Override
            public void onThrowable(Throwable e) {

            }

            @Override
            public void onError(String error) {
                reputationProductFragmentView.onErrorGetLikeDislikeReview(model, error);
            }

            @Override
            public void onNullData() {

            }

            @Override
            public void onNoConnection() {
                reputationProductFragmentView.onErrorConnectionGetLikeDislikeReview(model);
            }
        });
    }

    @Override
    public void postReport(@NonNull Context context,
                           @NonNull String reviewId,
                           @NonNull String shopId,
                           @NonNull String reportMessage) {
        actNetworkInteractor.postReport(context,
                actNetworkInteractor.getReportParam(reviewId,
                        shopId,
                        reportMessage),
                new ActReputationRetrofitInteractor.ActReputationListener() {
            @Override
            public void onSuccess(ActResult result) {
                reputationProductFragmentView.onSuccessLikeDislikeReview(result);
            }

            @Override
            public void onTimeout() {
                reputationProductFragmentView.onErrorConnection();
            }

            @Override
            public void onFailAuth() {

            }

            @Override
            public void onThrowable(Throwable e) {

            }

            @Override
            public void onError(String error) {
                reputationProductFragmentView.onError(error);
            }

            @Override
            public void onNullData() {

            }

            @Override
            public void onNoConnection() {
                reputationProductFragmentView.onErrorConnection();
            }
        });
    }

    @Override
    public void deleteComment(@NonNull Context context,
                              @NonNull String reputationId,
                              int reviewId,
                              @NonNull String shopId) {
        actNetworkInteractor.deleteComment(context,
                actNetworkInteractor.getDeleteCommentParam(reputationId,
                        String.valueOf(reviewId),
                        shopId),
                new ActReputationRetrofitInteractor.ActReputationListener() {
            @Override
            public void onSuccess(ActResult result) {
                reputationProductFragmentView.onSuccessDeleteComment(result);
            }

            @Override
            public void onTimeout() {
                reputationProductFragmentView.onErrorConnection();
            }

            @Override
            public void onFailAuth() {

            }

            @Override
            public void onThrowable(Throwable e) {

            }

            @Override
            public void onError(String error) {
                reputationProductFragmentView.onError(error);
            }

            @Override
            public void onNullData() {

            }

            @Override
            public void onNoConnection() {
                reputationProductFragmentView.onErrorConnection();
            }
        });
    }
}
