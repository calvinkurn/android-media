package com.tokopedia.gm.statistic.data.source;


import com.tokopedia.gm.statistic.constant.GMTransactionTableSortBy;
import com.tokopedia.gm.statistic.constant.GMTransactionTableSortType;
import com.tokopedia.gm.statistic.data.source.cloud.GMStatCloud;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetShopCategory;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.table.GetBuyerTable;
import com.tokopedia.gm.statistic.data.source.cloud.model.table.GetProductTable;
import com.tokopedia.gm.statistic.data.source.cloud.model.table.GetTransactionTable;

import javax.inject.Inject;

import rx.Observable;



/**
 * @author normansyahputa on 5/18/17.
 */

public class GMStatDataSource {

    private GMStatCloud gmStatCloud;


    @Inject
    public GMStatDataSource(GMStatCloud gmStatCloud) {

        this.gmStatCloud = gmStatCloud;

    }

    public Observable<GetTransactionGraph> getTransactionGraph(String shopId, long startDate, long endDate) {
        return gmStatCloud.getTransactionGraph(shopId, startDate, endDate).map(new SimpleResponseMapper<GetTransactionGraph>());








    }

    public Observable<GetTransactionTable> getTransactionTable(String shopId, long startDate, long endDate,
                                                               int page, int pageSize,
                                                               @GMTransactionTableSortType final int sortType,
                                                               @GMTransactionTableSortBy final int sortBy) {
        return gmStatCloud.getTransactionTable(shopId, startDate, endDate,page, pageSize, sortType, sortBy).map(new SimpleResponseMapper<GetTransactionTable>());













































    }

    public Observable<GetProductGraph> getProductGraph(String shopId, long startDate, long endDate) {
        return gmStatCloud.getProductGraph(shopId, startDate, endDate).map(new SimpleResponseMapper<GetProductGraph>());








    }

    public Observable<GetPopularProduct> getPopularProduct(String shopId, long startDate, long endDate) {
        return gmStatCloud.getPopularProduct(shopId, startDate, endDate).map(new SimpleResponseMapper<GetPopularProduct>());








    }

    public Observable<GetBuyerGraph> getBuyerGraph(String shopId, long startDate, long endDate) {
        return gmStatCloud.getBuyerGraph(shopId, startDate, endDate).map(new SimpleResponseMapper<GetBuyerGraph>());









    }

    public Observable<GetKeyword> getKeywordModel(String categoryId) {
        return gmStatCloud.getKeywordModel(categoryId).map(new SimpleResponseMapper<GetKeyword>());








    }

    public Observable<GetShopCategory> getShopCategory(String shopId, long startDate, long endDate) {
        return gmStatCloud.getShopCategory(shopId, startDate, endDate).map(new SimpleResponseMapper<GetShopCategory>());






















































    }

    public Observable<GetProductTable> getProductTable(String shopId, long startDate, long endDate) {
        return gmStatCloud.getProductTable(shopId, startDate, endDate)
                .map(new SimpleResponseMapper<GetProductTable>());
    }

    public Observable<GetBuyerTable> getBuyerTable(String shopId, long startDate, long endDate) {
        return gmStatCloud.getBuyerTable(shopId, startDate, endDate)
                .map(new SimpleResponseMapper<GetBuyerTable>());
    }

}
