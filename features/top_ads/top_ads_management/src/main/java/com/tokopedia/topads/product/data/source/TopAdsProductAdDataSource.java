package com.tokopedia.topads.product.data.source;

import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.data.model.data.ProductAdBulkAction;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.product.data.source.cloud.TopAdsProductAdDataSourceCloud;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * Created by hadi.putra on 04/05/18.
 */

public class TopAdsProductAdDataSource {
    private final TopAdsProductAdDataSourceCloud dataSourceCloud;

    public TopAdsProductAdDataSource(TopAdsProductAdDataSourceCloud dataSourceCloud) {
        this.dataSourceCloud = dataSourceCloud;
    }

    public Observable<PageDataResponse<List<ProductAd>>> getProductAd(RequestParams requestParams){
        return dataSourceCloud.getProductAd(requestParams);
    }

    public Observable<ProductAdBulkAction> bulkAction(DataRequest<ProductAdBulkAction> dataRequest) {
        return dataSourceCloud.bulkAction(dataRequest);
    }
}
