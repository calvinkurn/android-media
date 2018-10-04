package com.tokopedia.topads.keyword.domain.interactor;

import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.keyword.data.model.cloud.bulkkeyword.DataBulkKeyword;
import com.tokopedia.topads.keyword.domain.repository.TopAdsKeywordRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hadi.putra on 14/05/18.
 */

public class TopAdsKeywordActionBulkUseCase extends UseCase<PageDataResponse<DataBulkKeyword>> {
    private static final String PARAM_KEY = "data";

    private final TopAdsKeywordRepository repository;

    @Inject
    public TopAdsKeywordActionBulkUseCase(TopAdsKeywordRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<PageDataResponse<DataBulkKeyword>> createObservable(RequestParams requestParams) {
        return repository.bulkActionKeyword((DataRequest<DataBulkKeyword>) requestParams.getObject(PARAM_KEY));
    }

    public static RequestParams createRequestParams(DataRequest<DataBulkKeyword> dataRequest){
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(PARAM_KEY, dataRequest);
        return requestParams;
    }
}
