package com.tokopedia.tkpd.network.apiservices.recharge.apis;

import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.recharge.model.category.CategoryData;
import com.tokopedia.tkpd.recharge.model.operator.OperatorData;
import com.tokopedia.tkpd.recharge.model.product.ProductData;
import com.tokopedia.tkpd.recharge.model.recentNumber.RecentData;
import com.tokopedia.tkpd.recharge.model.recentOrder.LastOrder;
import com.tokopedia.tkpd.recharge.model.status.Status;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author ricoharisin on 7/4/16.
 */
public interface RechargeApi {

    @GET(TkpdBaseURL.Recharge.PATH_CATEGORY)
    Observable<Response<CategoryData>> getCategory();

    @GET(TkpdBaseURL.Recharge.PATH_PRODUCT)
    Observable<Response<ProductData>> getProduct();

    @GET(TkpdBaseURL.Recharge.PATH_OPERATOR)
    Observable<Response<OperatorData>> getOperator();

    @GET(TkpdBaseURL.Recharge.PATH_STATUS)
    Observable<Response<Status>> getStatus();

    @GET(TkpdBaseURL.Recharge.PATH_RECENT_NUMBER)
    Observable<Response<RecentData>> getRecentNumbers(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Recharge.PATH_LAST_ORDER)
    Observable<Response<LastOrder>> getLastOrder(@QueryMap Map<String, String> params);
}