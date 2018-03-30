package com.tokopedia.core.analytics.data;

import com.tokopedia.anals.ConsumerDrawerData;
import com.tokopedia.anals.SellerDrawerData;
import com.tokopedia.core.base.domain.RequestParams;

import rx.Observable;

/**
 * Created by Herdi_WORK on 03.10.17.
 */

public interface UserAttributesRepository {

    Observable<ConsumerDrawerData.Data> getConsumerUserAttributes(RequestParams parameters);

    Observable<SellerDrawerData.Data> getSellerUserAttributes(RequestParams parameters);

}
