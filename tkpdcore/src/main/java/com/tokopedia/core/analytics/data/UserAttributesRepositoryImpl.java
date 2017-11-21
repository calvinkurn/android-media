package com.tokopedia.core.analytics.data;

import com.tokopedia.anals.UserAttribute;
import com.tokopedia.core.analytics.data.factory.UserAttributesFactory;
import com.tokopedia.core.base.domain.RequestParams;

import rx.Observable;

/**
 * Created by Herdi_WORK on 03.10.17.
 */

public class UserAttributesRepositoryImpl implements UserAttributesRepository {

    private UserAttributesFactory userFactory;

    public UserAttributesRepositoryImpl(UserAttributesFactory userAttributesFactory){
        userFactory = userAttributesFactory;
    }

    @Override
    public Observable<UserAttribute.Data> getUserAttributes(RequestParams parameters) {
        return userFactory.createCloudAttrDataSource().getUserAttributes(parameters);
    }
}
