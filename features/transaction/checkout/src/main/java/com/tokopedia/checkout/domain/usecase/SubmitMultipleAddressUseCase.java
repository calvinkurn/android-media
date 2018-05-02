package com.tokopedia.checkout.domain.usecase;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.data.entity.response.shippingaddress.ShippingAddressDataResponse;
import com.tokopedia.checkout.data.repository.ICartRepository;
import com.tokopedia.checkout.domain.datamodel.cartmultipleshipment.SetShippingAddressData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kris on 2/28/18. Tokopedia
 */

public class SubmitMultipleAddressUseCase extends UseCase<SetShippingAddressData> {
    public static final String PARAM_REQUEST_AUTH_MAP_STRING = "PARAM_REQUEST_AUTH_MAP_STRING";

    private ICartRepository repository;

    @Inject
    public SubmitMultipleAddressUseCase(ICartRepository repository) {
        this.repository = repository;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<SetShippingAddressData> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> mapParam =
                (TKPDMapParam<String, String>) requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING);
        mapParam.putAll(requestParams.getParamsAllValueInString());
        return repository.shippingAddress(mapParam)
                .map(new Func1<ShippingAddressDataResponse, SetShippingAddressData>() {
                    @Override
                    public SetShippingAddressData call(ShippingAddressDataResponse shippingAddressDataResponse) {
                        return new SetShippingAddressData.Builder()
                                .success(shippingAddressDataResponse.getSuccess() == 1)
                                .build();
                    }
                });
    }
}
