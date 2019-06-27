package com.tokopedia.district_recommendation.domain.usecase;

import com.tokopedia.district_recommendation.data.repository.DistrictRecommendationRepository;
import com.tokopedia.district_recommendation.domain.model.AddressResponse;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Irfan Khoirul on 17/11/17.
 */

public class GetDistrictRequestUseCase extends UseCase<AddressResponse> {

    public static final String PARAM_TOKEN = "token";
    public static final String PARAM_UT = "ut";
    public static final String PARAM_QUERY = "query";
    public static final String PARAM_PAGE = "page";

    private final DistrictRecommendationRepository repository;

    @Inject
    public GetDistrictRequestUseCase(DistrictRecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<AddressResponse> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.putAll(requestParams.getParamsAllValueInString());
        return repository.getAddresses(param);
    }

}
