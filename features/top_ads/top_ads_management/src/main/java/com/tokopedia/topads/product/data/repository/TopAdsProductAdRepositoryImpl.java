package com.tokopedia.topads.product.data.repository;

import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.data.model.data.ProductAdBulkAction;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.product.data.source.TopAdsProductAdDataSource;
import com.tokopedia.topads.product.domain.repository.TopAdsProductAdRepository;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * Created by hadi.putra on 04/05/18.
 */

public class TopAdsProductAdRepositoryImpl implements TopAdsProductAdRepository {
    private final TopAdsProductAdDataSource dataSource;

    public TopAdsProductAdRepositoryImpl(TopAdsProductAdDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Observable<PageDataResponse<List<ProductAd>>> getProductAd(RequestParams requestParams) {
        return dataSource.getProductAd(requestParams);
    }

    @Override
    public Observable<ProductAdBulkAction> bulkAction(DataRequest<ProductAdBulkAction> dataRequest) {
        return dataSource.bulkAction(dataRequest);
    }
}
