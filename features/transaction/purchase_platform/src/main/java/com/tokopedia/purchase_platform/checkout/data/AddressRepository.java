package com.tokopedia.purchase_platform.checkout.data;

import java.util.Map;

import rx.Observable;

public interface AddressRepository {

    Observable<String> editAddress(Map<String, String> param);

}
