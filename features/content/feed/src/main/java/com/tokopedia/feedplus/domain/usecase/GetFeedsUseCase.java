package com.tokopedia.feedplus.domain.usecase;

import com.tokopedia.feedplus.data.repository.FeedRepository;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public class GetFeedsUseCase extends UseCase<FeedResult> {
    public static final String PARAM_USER_ID = "PARAM_USER_ID";
    public static final String PARAM_CURSOR = "PARAM_CURSOR";
    public static final String PARAM_PAGE = "PARAM_PAGE";
    protected FeedRepository feedRepository;

    @Inject
    public GetFeedsUseCase(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    @Override
    public Observable<FeedResult> createObservable(RequestParams requestParams) {
        return feedRepository.getFeedsFromCloud(requestParams);
    }

    public RequestParams getFeedPlusParam(int page, UserSessionInterface userSession, String
            currentCursor) {
        RequestParams params = RequestParams.create();
        params.putInt(GetFeedsUseCase.PARAM_USER_ID, Integer.parseInt(userSession.getUserId()));
        params.putString(GetFeedsUseCase.PARAM_CURSOR, currentCursor);
        params.putInt(GetFeedsUseCase.PARAM_PAGE, page);
        return params;
    }

    public RequestParams getRefreshParam(UserSessionInterface userSession) {
        RequestParams params = RequestParams.create();
        params.putInt(GetFeedsUseCase.PARAM_USER_ID, Integer.parseInt(userSession.getUserId()));
        params.putString(GetFeedsUseCase.PARAM_CURSOR, "");
        params.putInt(GetFeedsUseCase.PARAM_PAGE, 1);
        return params;
    }

}
