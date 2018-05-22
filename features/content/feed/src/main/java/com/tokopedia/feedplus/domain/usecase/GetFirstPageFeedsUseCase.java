package com.tokopedia.feedplus.domain.usecase;

import com.tokopedia.feedplus.data.repository.FeedRepository;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public class GetFirstPageFeedsUseCase extends GetFeedsUseCase {

    private GetFirstPageFeedsCloudUseCase getFirstPageFeedsCloudUseCase;

    public GetFirstPageFeedsUseCase(FeedRepository feedRepository,
                                    GetFirstPageFeedsCloudUseCase getFirstPageFeedsCloudUseCase) {
        super(feedRepository);
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
