package com.tokopedia.core.drawer2.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.drawer2.data.pojo.UserDrawerData;

import rx.Observable;

/**
 * Created by Herdi_WORK on 03.10.17.
 */

public interface UserAttributesRepository {

    Observable<UserDrawerData> getConsumerUserAttributes(RequestParams parameters);

    Observable<UserDrawerData> getSellerUserAttributes(RequestParams parameters);

}
