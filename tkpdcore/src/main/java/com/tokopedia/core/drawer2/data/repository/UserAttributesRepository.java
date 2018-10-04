package com.tokopedia.core.drawer2.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.drawer2.data.pojo.UserData;

import rx.Observable;

/**
 * Created by Herdi_WORK on 03.10.17.
 *
 *  Moved to account-home
 */
@Deprecated
public interface UserAttributesRepository {

    Observable<UserData> getConsumerUserAttributes(RequestParams parameters);

    Observable<UserData> getSellerUserAttributes(RequestParams parameters);

}
