package com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.ActReviewPass;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.repository.DeleteCommentRepository;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.ActResultDomain;

import rx.Observable;

/**
 * Created by yoasfs on 18/07/17.
 * @deprecated use DeleteReviewResponseUseCase instead
 */
@Deprecated
public class DeleteCommentUseCase extends UseCase<ActResultDomain> {
    protected DeleteCommentRepository deleteCommentRepository;

    public DeleteCommentUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                DeleteCommentRepository deleteCommentRepository) {
        super(threadExecutor, postExecutionThread);
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