package com.tokopedia.logisticdata.data.repository;

import com.tokopedia.logisticdata.data.apiservice.InsuranceApi;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Irfan Khoirul on 11/12/17.
 */

public class InsuranceTnCDataStore {

    private final InsuranceApi insuranceApi;

    @Inject
    public InsuranceTnCDataStore(InsuranceApi insuranceApi) {
        this.insuranceApi = insuranceApi;
    }

    Observable<String> getInsuranceTnC() {
        return insuranceApi.getInsuranceTnC();
    }

}
