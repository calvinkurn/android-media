package com.tokopedia.topads.product.domain.usecase;

import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.data.model.request.SearchAdRequest;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.product.domain.repository.TopAdsProductAdRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hadi.putra on 04/05/18.
 */

public class TopAdsGetProductAdUseCase extends UseCase<PageDataResponse<List<ProductAd>>> {
    private final TopAdsProductAdRepository repository;

    @Inject
    public TopAdsGetProductAdUseCase(TopAdsProductAdRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<PageDataResponse<List<ProductAd>>> createObservable(RequestParams requestParams) {
        return repository.getProductAd(requestParams);
    }

    public static RequestParams createRequestParams(SearchAdRequest searchAdRequest){
        RequestParams requestParams = RequestParams.create();
        for (String key : searchAdRequest.getParams().keySet()){
            requestParams.putString(key, searchAdRequest.getParams().get(key));
        }

        return requestParams;
    }
}
