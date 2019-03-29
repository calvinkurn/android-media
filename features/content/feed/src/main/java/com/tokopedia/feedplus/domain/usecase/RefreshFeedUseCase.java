package com.tokopedia.feedplus.domain.usecase;

import com.tokopedia.feedplus.data.repository.FeedRepository;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 7/5/17.
 */

public class RefreshFeedUseCase extends GetFeedsUseCase {

    @Inject
    public RefreshFeedUseCase(FeedRepository feedRepository) {
        super(feedRepository);
    }

    @Override
    public Observable<FeedResult> createObservable(final RequestParams requestParams) {
        return feedRepository.getFirstPageFeedsFromCloud(requestParams);

    }
}
