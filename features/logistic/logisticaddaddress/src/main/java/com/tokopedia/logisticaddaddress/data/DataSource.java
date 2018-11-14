package com.tokopedia.logisticaddaddress.data;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.logisticdata.data.entity.address.GetPeopleAddress;
import com.tokopedia.usecase.RequestParams;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by Fajar Ulin Nuha on 13/11/18.
 */
public interface DataSource {

    Observable<GetPeopleAddress> getAddress(RequestParams requestParams);

}
