package com.tokopedia.feedplus.domain.usecase;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.feedplus.data.repository.FeedRepository;
import com.tokopedia.feedplus.domain.model.CheckFeedDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by nisie on 8/23/17.
 */

public class CheckNewFeedUseCase extends UseCase<CheckFeedDomain> {

    public static final String PARAM_USER_ID = "PARAM_USER_ID";
    public static final String PARAM_CURSOR = "PARAM_CURSOR";
    private FeedRepository feedRepository;

    public CheckNewFeedUseCase(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    @Override
    public Observable<CheckFeedDomain> createObservable(RequestParams requestParams) {
        return feedRepository.checkNewFeed(requestParams);
    }

    public static RequestParams getParam(SessionHandler sessionHandler, String firstCursor) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_USER_ID, sessionHandler.getLoginID());
        params.putString(PARAM_CURSOR, firstCursor);
        return params;
    }
}
