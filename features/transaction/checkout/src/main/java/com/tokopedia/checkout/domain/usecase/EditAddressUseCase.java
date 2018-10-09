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

    public static final class Params {
        public static final String ADDRESS_ID = "address_id";
        public static final String ADDRESS_NAME = "address_name";
        public static final String ADDRESS_STREET = "address_street";
        public static final String POSTAL_CODE = "postal_code";
        public static final String DISTRICT_ID = "district";
        public static final String CITY_ID = "city";
        public static final String PROVINCE_ID = "province";
        public static final String RECEIVER_NAME = "receiver_name";
        public static final String RECEIVER_PHONE = "receiver_phone";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";

        private Params() {
        }
    }

}
