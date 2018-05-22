package com.tokopedia.feedplus.domain.usecase;

import com.tokopedia.feedplus.data.repository.FeedRepository;
import com.tokopedia.feedplus.domain.model.recentview.RecentViewProductDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * @author Kulomady on 12/8/16.
 */

public class GetRecentViewUseCase extends UseCase<List<RecentViewProductDomain>> {
    public static final String PARAM_USER_ID = "user_id";

    private final FeedRepository feedRepository;

    public GetRecentViewUseCase(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }


    @Override
    public Observable<List<RecentViewProductDomain>> createObservable(RequestParams requestParams) {
        return feedRepository.getRecentViewProduct(requestParams);
    }

    public RequestParams getParam(String loginID) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_USER_ID, loginID);
        return params;
    }
}
