package com.tokopedia.reviewseller.feature.reputationhistory.data.source;

import com.tokopedia.reviewseller.feature.reputationhistory.domain.model.SellerReputationDomain;
import com.tokopedia.usecase.RequestParams;

import java.util.Map;

import rx.Observable;

/**
 * @author normansyahputa on 3/16/17.
 */

public interface ReputationReviewDataSource {
    Observable<SellerReputationDomain> getReputationHistory(String shopId, Map<String, String> param);

    Observable<SellerReputationDomain> getReputationHistory(RequestParams requestParams);
}
