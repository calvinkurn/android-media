package com.tokopedia.topads.group.domain.usecase;

import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.topads.dashboard.data.model.data.GroupAdBulkAction;
import com.tokopedia.topads.group.domain.repository.TopAdsGroupAdRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hadi.putra on 11/05/18.
 */

public class TopAdsToggleStatusUseCase extends UseCase<GroupAdBulkAction> {
    private static final String PARAM_KEY = "data";

    private final TopAdsGroupAdRepository topAdsGroupAdRepository;

    @Inject
    public TopAdsToggleStatusUseCase(TopAdsGroupAdRepository topAdsGroupAdRepository) {
        this.topAdsGroupAdRepository = topAdsGroupAdRepository;
    }

    @Override
    public Observable<GroupAdBulkAction> createObservable(RequestParams requestParams) {
        return topAdsGroupAdRepository.bulkAction((DataRequest<GroupAdBulkAction>) requestParams.getObject(PARAM_KEY));
    }

    public static RequestParams createRequestParams(DataRequest<GroupAdBulkAction> dataRequest){
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(PARAM_KEY, dataRequest);
        return requestParams;
    }
}
