package com.tokopedia.gm.statistic.data.source.cloud.api;

import com.tokopedia.gm.statistic.constant.StatisticConstant;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetShopCategory;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.table.GetBuyerTable;
import com.tokopedia.gm.statistic.data.source.cloud.model.table.GetProductTable;
import com.tokopedia.gm.statistic.data.source.cloud.model.table.GetTransactionTable;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author normansyahputa on 7/6/17.
 */
public interface GMStatApi {
    @GET(StatisticConstant.GET_TRANSACTION_GRAPH_URL)
    Observable<Response<GetTransactionGraph>> getTransactionGraph(@Path("id") String shopId,
                                                                  @QueryMap Map<String, String> params);

    @GET(StatisticConstant.GET_TRANSACTION_TABLE_URL)
    Observable<Response<GetTransactionTable>> getTransactionTable(@Path("id") String shopId,
                                                                  @QueryMap Map<String, String> params);

    @GET(StatisticConstant.GET_PRODUCT_GRAPH)
    Observable<Response<GetProductGraph>> getProductGraph(@Path("id") String shopId,
                                                          @QueryMap Map<String, String> params);


    @GET(StatisticConstant.GET_POPULAR_PRODUCT)
    Observable<Response<GetPopularProduct>> getPopularProduct(@Path("id") String shopId,
                                                              @QueryMap Map<String, String> params);

    @GET(StatisticConstant.GET_BUYER_GRAPH)
    Observable<Response<GetBuyerGraph>> getBuyerGraph(@Path("id") String shopId,
                                                      @QueryMap Map<String, String> params);

    @GET(StatisticConstant.GET_KEYWORD)
    Observable<Response<GetKeyword>> getKeyword(@Path("id") String categoryId,
                                                @QueryMap Map<String, String> params);

    @GET(StatisticConstant.GET_SHOP_CATEGORY)
    Observable<Response<GetShopCategory>> getShopCategory(@Path("id") String shopId,
                                                          @QueryMap Map<String, String> params);

    @GET(StatisticConstant.GET_PRODUCT_TABLE)
    Observable<Response<GetProductTable>> getProductTable(@Path("id") String shopId,
                                                          @QueryMap Map<String, String> params);

    @GET(StatisticConstant.GET_BUYER_TABLE)
    Observable<Response<GetBuyerTable>> getBuyerTable(@Path("id") String shopId,
                                                      @QueryMap Map<String, String> params);

}
