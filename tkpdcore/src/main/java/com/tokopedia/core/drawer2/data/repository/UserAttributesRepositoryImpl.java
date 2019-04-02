package com.tokopedia.core.drawer2.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.drawer2.data.factory.UserAttributesFactory;
import com.tokopedia.core.drawer2.data.pojo.UserData;

import rx.Observable;

/**
 * Created by Herdi_WORK on 03.10.17.
 *
 * Moved to account-home
 */
@Deprecated
public class UserAttributesRepositoryImpl implements UserAttributesRepository {

    private UserAttributesFactory userFactory;

    public UserAttributesRepositoryImpl(UserAttributesFactory userAttributesFactory) {
        userFactory = userAttributesFactory;
    }

    @Override
    public Observable<UserData> getConsumerUserAttributes(RequestParams parameters) {
        return userFactory.createCloudAttrDataSource().getConsumerUserAttributes(parameters);
    }

    @Override
    public Observable<UserData> getSellerUserAttributes(RequestParams parameters) {
        return userFactory.createCloudAttrDataSource().getSellerUserAttributes(parameters);
    }
}
