package com.tokopedia.digital_deals.domain;

import com.tokopedia.digital_deals.domain.model.DealsCategoryDomain;
import java.util.HashMap;
import java.util.List;

import rx.Observable;

public interface DealsRepository {

    Observable<List<DealsCategoryDomain>> getDeals(HashMap<String, Object> parameters);
}
