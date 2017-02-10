package com.tokopedia.tkpd.home.feed.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.home.feed.domain.FeedRepository;
import com.tokopedia.tkpd.home.feed.domain.model.ProductFeed;

import java.util.List;

import rx.Observable;

/**
 * @author Kulomady on 12/8/16.
 */

public class GetRecentProductUsecase extends UseCase<List<ProductFeed>> {
    private final FeedRepository feedRepository;

    public GetRecentProductUsecase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   FeedRepository feedRepository) {
        super(threadExecutor, postExecutionThread);
        this.feedRepository = feedRepository;
    }


    @Override
    public Observable<List<ProductFeed>> createObservable(RequestParams requestParams) {
        return feedRepository.getRecentViewProduct();
    }
}
