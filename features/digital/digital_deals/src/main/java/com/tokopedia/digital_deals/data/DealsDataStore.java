package com.tokopedia.digital_deals.data;


import com.tokopedia.digital_deals.data.entity.response.DealsResponseEntity;

import java.util.HashMap;

import rx.Observable;



public interface DealsDataStore {

    Observable<DealsResponseEntity> getDeals(HashMap<String, Object> params);


}
