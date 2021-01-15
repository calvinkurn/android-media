package com.tokopedia.logisticaddaddress.data;

import com.tokopedia.logisticCommon.data.entity.address.GetPeopleAddress;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by Fajar Ulin Nuha on 13/11/18.
 */
public interface DataSource {

    Observable<GetPeopleAddress> getAddress(RequestParams requestParams);

}
