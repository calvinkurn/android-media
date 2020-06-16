package com.tokopedia.purchase_platform.common.feature.editaddress.data.repository;

import java.util.Map;

import rx.Observable;

public interface AddressRepository {

    Observable<String> editAddress(Map<String, String> param);

}
