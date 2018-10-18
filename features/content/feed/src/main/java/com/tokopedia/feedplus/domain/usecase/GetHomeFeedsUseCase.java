package com.tokopedia.feedplus.domain.usecase;


import com.tokopedia.feedplus.data.repository.HomeFeedRepository;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by henrypriyono on 12/29/17.
 */

public class GetHomeFeedsUseCase extends UseCase<FeedResult> {

    private final HomeFeedRepository feedRepository;

    public GetHomeFeedsUseCase(HomeFeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    @Override
    public Observable<FeedResult> createObservable(RequestParams requestParams) {
        return feedRepository.getHomeFeeds(requestParams);
    }

    public RequestParams getFeedPlusParam(int page, String userId, String
            currentCursor) {
        RequestParams params = RequestParams.create();
        params.putInt(GetFeedsUseCase.PARAM_USER_ID, Integer.parseInt(userId));
        params.putString(GetFeedsUseCase.PARAM_CURSOR, currentCursor);
        params.putInt(GetFeedsUseCase.PARAM_PAGE, page);
        return params;
    }
}
