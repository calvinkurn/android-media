package com.tokopedia.topads.product.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.data.model.data.ProductAdBulkAction;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.product.data.apiservice.TopAdsProductAdApi;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by hadi.putra on 04/05/18.
 */

public class TopAdsProductAdDataSourceCloud {
    private final TopAdsProductAdApi topAdsProductAdApi;

    public TopAdsProductAdDataSourceCloud(TopAdsProductAdApi topAdsProductAdApi) {
        this.topAdsProductAdApi = topAdsProductAdApi;
    }

    public Observable<PageDataResponse<List<ProductAd>>> getProductAd(RequestParams requestParams){
        return topAdsProductAdApi.getProductAd(requestParams.getParamsAllValueInString())
                .map(new Func1<Response<PageDataResponse<List<ProductAd>>>, PageDataResponse<List<ProductAd>>>() {
                    @Override
                    public PageDataResponse<List<ProductAd>> call(Response<PageDataResponse<List<ProductAd>>> pageDataResponse) {
                        return pageDataResponse.body();
                    }
                });
    }

    public Observable<ProductAdBulkAction> bulkAction(DataRequest<ProductAdBulkAction> dataRequest) {
        return topAdsProductAdApi.bulkActionProductAd(dataRequest)
                .map(new Func1<Response<DataResponse<ProductAdBulkAction>>, ProductAdBulkAction>() {
            @Override
            public ProductAdBulkAction call(Response<DataResponse<ProductAdBulkAction>> dataResponse) {
                return dataResponse.body().getData();
            }
        });
    }
}
