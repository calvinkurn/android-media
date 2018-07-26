package com.tokopedia.topads.product.data.apiservice;

import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.data.model.data.ProductAdBulkAction;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by hadi.putra on 04/05/18.
 */

public interface TopAdsProductAdApi {
    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_PRODUCT)
    Observable<Response<PageDataResponse<List<ProductAd>>>> getProductAd(@QueryMap Map<String, String> params);

    @Headers({TopAdsNetworkConstant.CONTENT_TYPE_APPLICATION_JSON})
    @PATCH(TopAdsNetworkConstant.PATH_BULK_ACTION_PRODUCT_AD)
    Observable<Response<DataResponse<ProductAdBulkAction>>> bulkActionProductAd(@Body DataRequest<ProductAdBulkAction> body);
}
