package com.tokopedia.core.network.apiservices.recharge.apis;

import com.tokopedia.core.database.model.category.CategoryData;
import com.tokopedia.core.database.recharge.numberList.NumberList;
import com.tokopedia.core.database.recharge.operator.OperatorData;
import com.tokopedia.core.database.recharge.product.ProductData;
import com.tokopedia.core.database.recharge.recentNumber.RecentData;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;
import com.tokopedia.core.database.recharge.status.Status;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;

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

}