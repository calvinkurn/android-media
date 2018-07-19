package com.tokopedia.topads.product.domain.repository;

import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.data.model.data.ProductAdBulkAction;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * Created by hadi.putra on 04/05/18.
 */

public interface TopAdsProductAdRepository {
    public Observable<PageDataResponse<List<ProductAd>>> getProductAd(RequestParams requestParams);

    Observable<ProductAdBulkAction> bulkAction(DataRequest<ProductAdBulkAction> dataRequest);
}
