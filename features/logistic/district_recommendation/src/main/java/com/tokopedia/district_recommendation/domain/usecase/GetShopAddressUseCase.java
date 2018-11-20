package com.tokopedia.district_recommendation.domain.usecase;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.district_recommendation.data.repository.ShopAddressRepository;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;


/**
 * Created by Irfan Khoirul on 19/11/18.
 */

public class GetShopAddressUseCase extends UseCase<Response<TokopediaWsV4Response>> {

    private final ShopAddressRepository repository;

    @Inject
    public GetShopAddressUseCase(ShopAddressRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<Response<TokopediaWsV4Response>> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.putAll(requestParams.getParamsAllValueInString());
        return repository.getLocation(param);

    }

}
