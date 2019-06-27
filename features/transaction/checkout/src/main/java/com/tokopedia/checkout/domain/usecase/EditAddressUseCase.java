package com.tokopedia.checkout.domain.usecase;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.data.repository.AddressRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

public class EditAddressUseCase extends UseCase<String> {

    public static final String RESPONSE_DATA = "data";
    public static final String RESPONSE_IS_SUCCESS = "is_success";
    private final AddressRepository addressRepository;

    @Inject
    public EditAddressUseCase(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> mapParam = new TKPDMapParam<>();
        mapParam.putAll(requestParams.getParamsAllValueInString());

        return addressRepository.editAddress(mapParam);
    }

}
