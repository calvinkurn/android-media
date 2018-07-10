package com.tokopedia.feedplus.domain.usecase;

import com.tokopedia.feedplus.data.repository.FeedRepository;
import com.tokopedia.feedplus.domain.model.feeddetail.DataFeedDetailDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 5/24/17.
 */

public class GetFeedsDetailUseCase extends UseCase<List<DataFeedDetailDomain>> {

    public static final String PARAM_DETAIL_ID = "PARAM_DETAIL_ID";
    public static final String PARAM_PAGE = "PARAM_PAGE";
    public static final String PARAM_USER_ID = "PARAM_USER_ID";

    private FeedRepository feedRepository;

    @Inject
    public GetFeedsDetailUseCase(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    @Override
    public Observable<List<DataFeedDetailDomain>> createObservable(RequestParams requestParams) {
        return feedRepository.getFeedsDetail(requestParams);
    }

    public RequestParams getFeedDetailParam(String loginID, String detailId, int page) {
        RequestParams params = RequestParams.create();
        params.putString(GetFeedsDetailUseCase.PARAM_USER_ID, loginID);
        params.putString(GetFeedsDetailUseCase.PARAM_DETAIL_ID, detailId);
        params.putInt(GetFeedsDetailUseCase.PARAM_PAGE, page);
        return params;
    }
}
