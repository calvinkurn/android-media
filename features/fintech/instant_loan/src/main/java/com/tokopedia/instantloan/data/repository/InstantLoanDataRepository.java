package com.tokopedia.instantloan.data.repository;

import com.google.gson.JsonObject;
import com.tokopedia.instantloan.data.soruce.cloud.PhoneDetailsDataCloud;
import com.tokopedia.instantloan.domain.model.PhoneDataModelDomain;
import com.tokopedia.instantloan.domain.repository.InstantLoanDomainRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by lavekush on 21/03/18.
 */

public class InstantLoanDataRepository implements InstantLoanDomainRepository {

    private PhoneDetailsDataCloud mPhoneDetailsDataCloud;

    @Inject
    public InstantLoanDataRepository(PhoneDetailsDataCloud phoneDetailsDataCloud) {
        this.mPhoneDetailsDataCloud = phoneDetailsDataCloud;
    }

    @Override
    public Observable<PhoneDataModelDomain> postPhoneData(JsonObject body) {
        return mPhoneDetailsDataCloud.postPhoneData(body);
    }
}
