package com.tokopedia.feedplus.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.feedplus.data.repository.FeedRepository;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;

import rx.Observable;

/**
 * @author by nisie on 7/5/17.
 */

public class RefreshFeedUseCase extends GetFeedsUseCase {

    public RefreshFeedUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              FeedRepository feedRepository) {
        super(threadExecutor, postExecutionThread, feedRepository);
    }

    @Override
    public Observable<FeedResult> createObservable(final RequestParams requestParams) {
        return feedRepository.getFirstPageFeedsFromCloud(requestParams);

    }
}
