package com.tokopedia.feedplus.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.feedplus.data.repository.FeedRepository;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public class GetFirstPageFeedsUseCase extends GetFeedsUseCase {

    GetFirstPageFeedsCloudUseCase getFirstPageFeedsCloudUseCase;

    public GetFirstPageFeedsUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    FeedRepository feedRepository,
                                    GetFirstPageFeedsCloudUseCase getFirstPageFeedsCloudUseCase) {
        super(threadExecutor, postExecutionThread, feedRepository);
        this.getFirstPageFeedsCloudUseCase = getFirstPageFeedsCloudUseCase;
    }

    @Override
    public Observable<FeedResult> createObservable(final RequestParams requestParams) {
        return Observable.concat(
                feedRepository.getFirstPageFeedsFromLocal(),
                getFirstPageFeedsCloudUseCase.createObservable(requestParams))
                .onErrorResumeNext(getFirstPageFeedsCloudUseCase.createObservable(requestParams));
    }
}
