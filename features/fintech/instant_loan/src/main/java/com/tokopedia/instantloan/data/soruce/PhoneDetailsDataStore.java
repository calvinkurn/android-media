package com.tokopedia.instantloan.data.soruce;

import com.google.gson.JsonObject;
import com.tokopedia.instantloan.domain.model.PhoneDataModelDomain;

import rx.Observable;

/**
 * Created by lavekush on 22/03/18.
 */

public interface PhoneDetailsDataStore {

    Observable<PhoneDataModelDomain> postPhoneData(JsonObject data);
}
