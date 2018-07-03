package com.tokopedia.instantloan.domain.repository;

import com.google.gson.JsonObject;
import com.tokopedia.instantloan.domain.model.PhoneDataModelDomain;

import rx.Observable;

/**
 * Created by lavekush on 21/03/18.
 */

public interface InstantLoanDomainRepository {

    Observable<PhoneDataModelDomain> postPhoneData(JsonObject body);

}
