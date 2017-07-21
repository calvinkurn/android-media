package com.tokopedia.core.reputationproduct.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.reputationproduct.domain.usecase.GetLikeDislikeUseCase;
import com.tokopedia.core.reputationproduct.domain.usecase.LikeDislikeUseCase;
import com.tokopedia.core.reputationproduct.fragment.ReputationProductFragmentView;
import com.tokopedia.core.reputationproduct.view.subscriber.ActionLikeDislikeSubscriber;
import com.tokopedia.core.reputationproduct.view.subscriber.GetLikeDislikeSubscriber;
import com.tokopedia.core.review.model.product_review.ReviewProductModel;

/**
 * Created by yoasfs on 13/07/17.
 */

public class ReputationProductViewFragmentPresenterImpl implements ReputationProductViewFragmentPresenter {

    private ReputationProductFragmentView reputationProductFragmentView;
//    private ActReputationRetrofitInteractor actNetworkInteractor;
//    private InboxReputationRetrofitInteractor networkInteractor;
    private GetLikeDislikeUseCase getLikeDislikeUseCase;
    private LikeDislikeUseCase likeDislikeUseCase;
//    public ReputationProductViewFragmentPresenterImpl(ReputationProductFragmentView reputationProductFragmentView,
//                                                      ActReputationRetrofitInteractor actNetworkInteractor,
//                                                      InboxReputationRetrofitInteractor networkInteractor) {
//
//        this.reputationProductFragmentView = reputationProductFragmentView;
//        this.actNetworkInteractor = actNetworkInteractor;
//        this.networkInteractor = networkInteractor;
//    }

    public ReputationProductViewFragmentPresenterImpl(ReputationProductFragmentView reputationProductFragmentView,
                                                      GetLikeDislikeUseCase getLikeDislikeUseCase,
                                                      LikeDislikeUseCase likeDislikeUseCase) {
        this.reputationProductFragmentView = reputationProductFragmentView;
        this.getLikeDislikeUseCase = getLikeDislikeUseCase;
        this.likeDislikeUseCase = likeDislikeUseCase;
    }

    @Override
    public void getLikeDislike(@NonNull Context context,
                               @NonNull String shopId,
                               @NonNull String reviewId) {
        getLikeDislikeUseCase.execute(getLikeDislikeUseCase.getLikeDislikeRequestParams(shopId,
                reviewId),
                new GetLikeDislikeSubscriber(reputationProductFragmentView));
    }

    @Override
    public void updateFacade(@NonNull Context context,
                             int reviewId,
                             @NonNull String productId,
                             @NonNull String shopId,
                             int statusLikeDislike,
                             @NonNull final ReviewProductModel model) {
        likeDislikeUseCase.execute(likeDislikeUseCase.getActionLikeDislikeParam(String.valueOf(reviewId),
                productId,
                shopId,
                String.valueOf(shopId)),
                new ActionLikeDislikeSubscriber(reputationProductFragmentView, model));
//        actNetworkInteractor.likeDislikeReview(context,
//                actNetworkInteractor.getActionLikeDislikeParam(String.valueOf(reviewId),
//                        productId,
//                        shopId,
//                        String.valueOf(statusLikeDislike)),
//                new ActReputationRetrofitInteractor.ActReputationListener() {
//            @Override
//            public void onSuccess(ActResult result) {
//                reputationProductFragmentView.onSuccessGetLikeDislikeReview();
//            }
//
//            @Override
//            public void onTimeout() {
//                reputationProductFragmentView.onErrorConnectionGetLikeDislikeReview(model);
//            }
//
//            @Override
//            public void onFailAuth() {
//
//            }
//
//            @Override
//            public void onThrowable(Throwable e) {
//
//            }
//
//            @Override
//            public void onError(String error) {
//                reputationProductFragmentView.onErrorGetLikeDislikeReview(model, error);
//            }
//
//            @Override
//            public void onNullData() {
//
//            }
//
//            @Override
//            public void onNoConnection() {
//                reputationProductFragmentView.onErrorConnectionGetLikeDislikeReview(model);
//            }
//        });
    }

    @Override
    public void postReport(@NonNull Context context,
                           @NonNull String reviewId,
                           @NonNull String shopId,
                           @NonNull String reportMessage) {
//        actNetworkInteractor.postReport(context,
//                actNetworkInteractor.getReportParam(reviewId,
//                        shopId,
//                        reportMessage),
//                new ActReputationRetrofitInteractor.ActReputationListener() {
//            @Override
//            public void onSuccess(ActResult result) {
//                reputationProductFragmentView.onSuccessLikeDislikeReview(result);
//            }
//
//            @Override
//            public void onTimeout() {
//                reputationProductFragmentView.onErrorConnection();
//            }
//
//            @Override
//            public void onFailAuth() {
//
//            }
//
//            @Override
//            public void onThrowable(Throwable e) {
//
//            }
//
//            @Override
//            public void onError(String error) {
//                reputationProductFragmentView.onError(error);
//            }
//
//            @Override
//            public void onNullData() {
//
//            }
//
//            @Override
//            public void onNoConnection() {
//                reputationProductFragmentView.onErrorConnection();
//            }
//        });
    }

    @Override
    public void deleteComment(@NonNull Context context,
                              @NonNull String reputationId,
                              int reviewId,
                              @NonNull String shopId) {
//        actNetworkInteractor.deleteComment(context,
//                actNetworkInteractor.getDeleteCommentParam(reputationId,
//                        String.valueOf(reviewId),
//                        shopId),
//                new ActReputationRetrofitInteractor.ActReputationListener() {
//            @Override
//            public void onSuccess(ActResult result) {
//                reputationProductFragmentView.onSuccessDeleteComment(result);
//            }
//
//            @Override
//            public void onTimeout() {
//                reputationProductFragmentView.onErrorConnection();
//            }
//
//            @Override
//            public void onFailAuth() {
//
//            }
//
//            @Override
//            public void onThrowable(Throwable e) {
//
//            }
//
//            @Override
//            public void onError(String error) {
//                reputationProductFragmentView.onError(error);
//            }
//
//            @Override
//            public void onNullData() {
//
//            }
//
//            @Override
//            public void onNoConnection() {
//                reputationProductFragmentView.onErrorConnection();
//            }
//        });
    }
}
