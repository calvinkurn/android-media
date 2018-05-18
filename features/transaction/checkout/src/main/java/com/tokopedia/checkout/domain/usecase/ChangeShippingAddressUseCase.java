package com.tokopedia.checkout.domain.usecase;

import com.tokopedia.checkout.domain.datamodel.cartmultipleshipment.SetShippingAddressData;
import com.tokopedia.transactiondata.entity.response.shippingaddress.ShippingAddressDataResponse;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kris on 2/28/18. Tokopedia
 */

public class ChangeShippingAddressUseCase extends UseCase<SetShippingAddressData> {
    public static final String PARAM_REQUEST_AUTH_MAP_STRING = "PARAM_REQUEST_AUTH_MAP_STRING";

    private ICartRepository repository;

    @Inject
    public ChangeShippingAddressUseCase(ICartRepository repository) {
        this.repository = repository;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<SetShippingAddressData> createObservable(RequestParams requestParams) {
        return repository.setShippingAddress(requestParams.getParamsAllValueInString())
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
