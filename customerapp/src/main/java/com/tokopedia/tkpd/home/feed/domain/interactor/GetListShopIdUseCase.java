package com.tokopedia.tkpd.home.feed.domain.interactor;

import com.tokopedia.core.base.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.home.feed.domain.FeedRepository;

import java.util.List;

import rx.Observable;

/**
 * @author Kulomady on 12/8/16.
 */

public class GetListShopIdUseCase extends UseCase<List<String>> {

    private FeedRepository mFeedRepository;

    public GetListShopIdUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                FeedRepository feedRepository) {
        super(threadExecutor, postExecutionThread);
        mFeedRepository = feedRepository;
    }

    @Override
    public Observable<List<String>> createObservable() {
        return mFeedRepository.getListShopId();
    }
}
