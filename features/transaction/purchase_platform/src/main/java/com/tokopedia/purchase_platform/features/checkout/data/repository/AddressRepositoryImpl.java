package com.tokopedia.purchase_platform.features.checkout.data.repository;

import com.tokopedia.logisticdata.data.apiservice.PeopleActApi;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

public class AddressRepositoryImpl implements AddressRepository {

    private final PeopleActApi peopleActApi;

    @Inject
    public AddressRepositoryImpl(PeopleActApi peopleActApi) {
        this.peopleActApi = peopleActApi;
    }

    @Override
    public Observable<String> editAddress(Map<String, String> param) {
        return peopleActApi.editAddress(param);
    }

}
