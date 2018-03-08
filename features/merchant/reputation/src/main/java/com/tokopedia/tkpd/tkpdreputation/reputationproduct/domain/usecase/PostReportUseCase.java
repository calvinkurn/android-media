package com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.repository.PostReportRepository;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.ActResultDomain;

import rx.Observable;

/**
 * Created by yoasfs on 18/07/17.
 * @deprecated use ReportReviewUseCase instead
 */
@Deprecated
public class PostReportUseCase extends UseCase<ActResultDomain> {
    public static final String PARAM_REVIEW_ID = "review_id";
    public static final String PARAM_SHOP_ID = "shop_id";
    private static final String PARAM_REPORT_MESSAGE = "text_message";
    protected PostReportRepository postReportRepository;

    public PostReportUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             PostReportRepository postReportRepository) {
        super(threadExecutor, postExecutionThread);
        this.postReportRepository = postReportRepository;
    }

    @Override
    public Observable<ActResultDomain> createObservable(RequestParams requestParams) {
        return postReportRepository.getPostReportRepository(requestParams.getParamsAllValueInString());
    }

    public RequestParams getReportParam(String reviewId, String shopId, String reportMessage) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_REVIEW_ID, reviewId);
        requestParams.putString(PARAM_SHOP_ID, shopId);
        requestParams.putString(PARAM_REPORT_MESSAGE, reportMessage);
        return requestParams;
    }

}