package com.tokopedia.logisticdata.data.repository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Irfan Khoirul on 11/12/17.
 */

public class InsuranceTnCRepository {
    private final InsuranceTnCDataStore dataStore;

    @Inject
    public InsuranceTnCRepository(InsuranceTnCDataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Observable<String> getInsuranceTnc() {
        return dataStore.getInsuranceTnC();
    }

}
