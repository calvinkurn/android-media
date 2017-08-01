package com.tokopedia.core.reputationproduct.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.reputationproduct.domain.usecase.DeleteCommentUseCase;
import com.tokopedia.core.reputationproduct.domain.usecase.GetLikeDislikeUseCase;
import com.tokopedia.core.reputationproduct.domain.usecase.LikeDislikeUseCase;
import com.tokopedia.core.reputationproduct.domain.usecase.PostReportUseCase;
import com.tokopedia.core.reputationproduct.fragment.ReputationProductFragmentView;
import com.tokopedia.core.reputationproduct.view.subscriber.ActionLikeDislikeSubscriber;
import com.tokopedia.core.reputationproduct.view.subscriber.DeleteCommentSubscriber;
import com.tokopedia.core.reputationproduct.view.subscriber.GetLikeDislikeSubscriber;
import com.tokopedia.core.reputationproduct.view.subscriber.PostReportSubscriber;
import com.tokopedia.core.review.model.product_review.ReviewProductModel;

/**
 * Created by yoasfs on 13/07/17.
 */

public class ReputationProductViewFragmentPresenterImpl implements ReputationProductViewFragmentPresenter {

    private ReputationProductFragmentView reputationProductFragmentView;
    private GetLikeDislikeUseCase getLikeDislikeUseCase;
    private LikeDislikeUseCase likeDislikeUseCase;
    private PostReportUseCase postReportUseCase;
    private DeleteCommentUseCase deleteCommentUseCase;

    public ReputationProductViewFragmentPresenterImpl(ReputationProductFragmentView reputationProductFragmentView,
                                                      GetLikeDislikeUseCase getLikeDislikeUseCase,
                                                      LikeDislikeUseCase likeDislikeUseCase,
                                                      PostReportUseCase postReportUseCase,
                                                      DeleteCommentUseCase deleteCommentUseCase) {
        this.reputationProductFragmentView = reputationProductFragmentView;
        this.getLikeDislikeUseCase = getLikeDislikeUseCase;
        this.likeDislikeUseCase = likeDislikeUseCase;
        this.postReportUseCase = postReportUseCase;
        this.deleteCommentUseCase = deleteCommentUseCase;
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
    }

    @Override
    public void postReport(@NonNull Context context,
                           @NonNull String reviewId,
                           @NonNull String shopId,
                           @NonNull String reportMessage) {
        postReportUseCase.execute(postReportUseCase.getReportParam(reviewId,
                shopId,
                reportMessage),
                new PostReportSubscriber(reputationProductFragmentView));
    }

    @Override
    public void deleteComment(@NonNull Context context,
                              @NonNull String reputationId,
                              int reviewId,
                              @NonNull String shopId) {
        deleteCommentUseCase.execute(deleteCommentUseCase.getDeleteCommentParam(reputationId,
                String.valueOf(reviewId),
                shopId),
                new DeleteCommentSubscriber(reputationProductFragmentView));
    }
}
