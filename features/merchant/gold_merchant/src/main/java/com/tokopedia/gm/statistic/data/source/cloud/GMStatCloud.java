package com.tokopedia.gm.statistic.data.source.cloud;

import com.tokopedia.gm.statistic.constant.GMTransactionTableSortBy;
import com.tokopedia.gm.statistic.constant.GMTransactionTableSortType;
import com.tokopedia.gm.statistic.data.source.cloud.api.GMStatApi;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetShopCategory;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.table.GetBuyerTable;
import com.tokopedia.gm.statistic.data.source.cloud.model.table.GetProductTable;
import com.tokopedia.gm.statistic.data.source.cloud.model.table.GetTransactionTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class GMStatCloud {

    public static final String SHOP_ID = "shop_id";
    public static final String S_DATE = "start_date";
    public static final String E_DATE = "end_date";
    public static final String SORT_TYP = "sort_type";
    public static final String SORT_BY = "sort_by";
    public static final String PAGE = "page";
    public static final String PAGE_SIZE = "page_size";

    private GMStatApi gmStatApi;

    @Inject
    public GMStatCloud(GMStatApi gmStatApi) {
        this.gmStatApi = gmStatApi;
    }

    public Observable<Response<GetTransactionGraph>> getTransactionGraph(String shopId, long startDate, long endDate) {
        Map<String, String> param = generateStartEndDateMap(startDate, endDate);
        return gmStatApi.getTransactionGraph(shopId, param);
    }

    public Observable<Response<GetTransactionTable>> getTransactionTable(String shopId, long startDate, long endDate,
                                                                         int page, int pageSize,
                                                                         @GMTransactionTableSortType int sortType, @GMTransactionTableSortBy int sortBy) {
        Map<String, String> param = generateStartEndDateMap(startDate, endDate);
        if (sortType > -1) {
            param.put(SORT_TYP, String.valueOf(sortType));
        }
        if (sortBy > -1) {
            param.put(SORT_BY, String.valueOf(sortBy));
        }
        if (page > -1) {
            param.put(PAGE, String.valueOf(page));
        }
        if (pageSize > -1) {
            param.put(PAGE_SIZE, String.valueOf(pageSize));
        }
        return gmStatApi.getTransactionTable(shopId, param);
    }

    public Observable<Response<GetProductGraph>> getProductGraph(String shopId, long startDate, long endDate) {
        Map<String, String> param = generateStartEndDateMap(startDate, endDate);
        return gmStatApi.getProductGraph(shopId, param);
    }

    public Observable<Response<GetPopularProduct>> getPopularProduct(String shopId, long startDate, long endDate) {
        Map<String, String> param = generateStartEndDateMap(startDate, endDate);
        return gmStatApi.getPopularProduct(shopId, param);
    }

    public Observable<Response<GetBuyerGraph>> getBuyerGraph(String shopId, long startDate, long endDate) {
        Map<String, String> param = generateStartEndDateMap(startDate, endDate);
        return gmStatApi.getBuyerGraph(shopId, param);
    }

    public Observable<Response<GetKeyword>> getKeywordModel(String categoryId) {
        return gmStatApi.getKeyword(categoryId, new HashMap<String, String>());
    }

    public Observable<Response<GetShopCategory>> getShopCategory(String shopId, long startDate, long endDate) {
        Map<String, String> param = generateStartEndDateMap(startDate, endDate);
        return gmStatApi.getShopCategory(shopId, param).take(1);
    }

    public Observable<Response<GetProductTable>> getProductTable(String shopId, long startDate, long endDate) {
        Map<String, String> param = generateStartEndDateMap(startDate, endDate);
        return gmStatApi.getProductTable(shopId, param);
    }

    public Observable<Response<GetBuyerTable>> getBuyerTable(String shopId, long startDate, long endDate) {
        Map<String, String> param = generateStartEndDateMap(startDate, endDate);
        return gmStatApi.getBuyerTable(shopId, param);
    }

    private Map<String, String> generateStartEndDateMap(long startDate, long endDate) {
        Map<String, String> param = new HashMap<>();
        if (startDate > 0) {
            param.put(S_DATE, getFormattedDate(startDate));
        } else {
            param.remove(S_DATE);
        }

        if (endDate > 0) {
            param.put(E_DATE, getFormattedDate(endDate));
        } else {
            param.remove(E_DATE);
        }
        return param;
    }

    public String getFormattedDate(long dateLong) {
        Date date = new Date(dateLong);
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd", new Locale("in", "ID"));// "HH:mm:ss:SSS"
        return formatter.format(date);
    }
}
