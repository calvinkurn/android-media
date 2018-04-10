package com.tokopedia.digital_deals.data.source;

import com.tokopedia.digital_deals.data.entity.response.DealsResponseEntity;

import retrofit2.http.GET;
import rx.Observable;

public interface DealsApi {

    @GET(DealsUrl.DEALS_LIST)
    Observable<DealsResponseEntity> getDeals();
}
