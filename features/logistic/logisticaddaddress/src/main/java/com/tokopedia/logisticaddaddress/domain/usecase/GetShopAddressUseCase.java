package com.tokopedia.logisticaddaddress.domain.usecase;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.logisticaddaddress.data.repository.ShopAddressRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;


/**
 * Created by Irfan Khoirul on 19/11/18.
 */

public class GetShopAddressUseCase extends UseCase<Response<TokopediaWsV4Response>> {

    public static final String PARAM_AUTH = "PARAM_AUTH";

    private final ShopAddressRepository repository;

    @Inject
    public GetShopAddressUseCase(ShopAddressRepository repository) {
        this.repository = repository;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<Response<TokopediaWsV4Response>> createObservable(RequestParams requestParams) {
        Map<String, String> param = (Map<String, String>) requestParams.getObject(PARAM_AUTH);
        return repository.getLocation(param);
    }

}
