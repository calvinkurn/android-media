package com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.usecase;

import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.ActReviewPass;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.repository.DeleteCommentRepository;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.ActResultDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by yoasfs on 18/07/17.
 * @deprecated use DeleteReviewResponseUseCase instead
 */
@Deprecated
public class DeleteCommentUseCase extends UseCase<ActResultDomain> {
    protected DeleteCommentRepository deleteCommentRepository;

    public DeleteCommentUseCase(DeleteCommentRepository deleteCommentRepository) {
        super();
        this.deleteCommentRepository = deleteCommentRepository;
    }

    @Override
    public Observable<ActResultDomain> createObservable(RequestParams requestParams) {
        return deleteCommentRepository.deleteCommentRepository(requestParams.getParamsAllValueInString());
    }

    public RequestParams getDeleteCommentParam(String reputationId, String reviewId, String shopId) {
        ActReviewPass pass = new ActReviewPass();
        RequestParams requestParams = RequestParams.create();
        pass.setReputationId(reputationId);
        pass.setReviewId(String.valueOf(reviewId));
        pass.setShopId(shopId);
        requestParams = pass.getDeleteCommentParam();
        return requestParams;
    }

}