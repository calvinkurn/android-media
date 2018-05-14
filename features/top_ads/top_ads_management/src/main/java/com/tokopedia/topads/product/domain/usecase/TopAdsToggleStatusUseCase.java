package com.tokopedia.topads.product.domain.usecase;

import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.topads.dashboard.data.model.data.ProductAdBulkAction;
import com.tokopedia.topads.product.domain.repository.TopAdsProductAdRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hadi.putra on 11/05/18.
 */

public class TopAdsToggleStatusUseCase extends UseCase<ProductAdBulkAction> {
    private static final String PARAM_KEY = "data";

    private final TopAdsProductAdRepository topAdsProductAdRepository;

    @Inject
    public TopAdsToggleStatusUseCase(TopAdsProductAdRepository topAdsProductAdRepository) {
        this.topAdsProductAdRepository = topAdsProductAdRepository;
    }

    @Override
    public Observable<ProductAdBulkAction> createObservable(RequestParams requestParams) {
        return topAdsProductAdRepository.bulkAction((DataRequest<ProductAdBulkAction>) requestParams.getObject(PARAM_KEY));
    }

    public static RequestParams createRequestParams(DataRequest<ProductAdBulkAction> dataRequest){
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(PARAM_KEY, dataRequest);
        return requestParams;
    }
}
