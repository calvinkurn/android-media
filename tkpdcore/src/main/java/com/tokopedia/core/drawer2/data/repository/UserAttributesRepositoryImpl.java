package com.tokopedia.core.drawer2.data.repository;

import com.tokopedia.anals.ConsumerDrawerData;
import com.tokopedia.anals.SellerDrawerData;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.drawer2.data.factory.UserAttributesFactory;

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
    public Observable<ConsumerDrawerData.Data> getConsumerUserAttributes(RequestParams parameters) {
        return userFactory.createCloudAttrDataSource().getConsumerUserAttributes(parameters);
    }

    @Override
    public Observable<SellerDrawerData.Data> getSellerUserAttributes(RequestParams parameters) {
        return userFactory.createCloudAttrDataSource().getSellerUserAttributes(parameters);
    }
}
