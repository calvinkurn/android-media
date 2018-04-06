package com.tokopedia.core.drawer2.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.drawer2.data.factory.UserAttributesFactory;
import com.tokopedia.core.drawer2.data.pojo.SellerDrawerData;
import com.tokopedia.core.drawer2.data.pojo.UserDrawerData;

import rx.Observable;

/**
 * Created by Herdi_WORK on 03.10.17.
 */

public class UserAttributesRepositoryImpl implements UserAttributesRepository {

    private UserAttributesFactory userFactory;

    public UserAttributesRepositoryImpl(UserAttributesFactory userAttributesFactory) {
        userFactory = userAttributesFactory;
    }

    @Override
    public Observable<UserDrawerData> getConsumerUserAttributes(RequestParams parameters) {
        return userFactory.createCloudAttrDataSource().getConsumerUserAttributes(parameters);
    }

    @Override
    public Observable<SellerDrawerData> getSellerUserAttributes(RequestParams parameters) {
        return userFactory.createCloudAttrDataSource().getSellerUserAttributes(parameters);
    }
}
