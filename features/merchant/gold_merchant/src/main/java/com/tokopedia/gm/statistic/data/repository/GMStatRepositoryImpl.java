package com.tokopedia.gm.statistic.data.repository;

import com.tokopedia.gm.statistic.constant.GMTransactionTableSortBy;
import com.tokopedia.gm.statistic.constant.GMTransactionTableSortType;
import com.tokopedia.gm.statistic.data.source.GMStatDataSource;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetShopCategory;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.table.GetBuyerTable;
import com.tokopedia.gm.statistic.data.source.cloud.model.table.GetProductTable;
import com.tokopedia.gm.statistic.domain.GMStatRepository;
import com.tokopedia.gm.statistic.domain.mapper.GMTransactionStatDomainMapper;
import com.tokopedia.gm.statistic.domain.mapper.GMTransactionTableMapper;
import com.tokopedia.gm.statistic.domain.model.transaction.table.GetTransactionTableModel;
import com.tokopedia.gm.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.seller.shop.common.domain.repository.ShopInfoRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class GMStatRepositoryImpl implements GMStatRepository {

    private GMStatDataSource gmStatDataSource;
    private GMTransactionStatDomainMapper gmTransactionStatDomainMapper;
    private GMTransactionTableMapper gmTransactionTableMapper;
    private ShopInfoRepository shopInfoRepository;

    @Inject
    public GMStatRepositoryImpl(GMTransactionStatDomainMapper gmTransactionStatDomainMapper,
                                GMStatDataSource gmStatDataSource,
                                GMTransactionTableMapper gmTransactionTableMapper,
                                ShopInfoRepository shopInfoRepository) {
        this.gmStatDataSource = gmStatDataSource;
        this.gmTransactionStatDomainMapper = gmTransactionStatDomainMapper;
        this.gmTransactionTableMapper = gmTransactionTableMapper;
        this.shopInfoRepository = shopInfoRepository;
    }

    @Override
    public Observable<GMTransactionGraphMergeModel> getTransactionGraph(long startDate, long endDate) {
        String shopId = shopInfoRepository.getShopId();
        return gmStatDataSource.getTransactionGraph(shopId, startDate, endDate).map(gmTransactionStatDomainMapper);
    }

    public GMTransactionGraphMergeModel getTransactionGraph(GetTransactionGraph getTransactionGraph) {
        return Observable.just(getTransactionGraph)
                .map(gmTransactionStatDomainMapper).toBlocking().first();
    }

    @Override
    public Observable<GetTransactionTableModel> getTransactionTable(long startDate, long endDate,
                                                                    int page, int pageSize,
                                                                    @GMTransactionTableSortType int sortType, @GMTransactionTableSortBy int sortBy) {
        String shopId = shopInfoRepository.getShopId();
        return gmStatDataSource.getTransactionTable(shopId, startDate, endDate, page, pageSize,
                sortType, sortBy).map(gmTransactionTableMapper);
    }

    @Override
    public Observable<GetProductGraph> getProductGraph(long startDate, long endDate) {
        String shopId = shopInfoRepository.getShopId();
        return gmStatDataSource.getProductGraph(shopId, startDate, endDate);
    }

    @Override
    public Observable<GetPopularProduct> getPopularProduct(long startDate, long endDate) {
        String shopId = shopInfoRepository.getShopId();
        return gmStatDataSource.getPopularProduct(shopId, startDate, endDate);
    }

    @Override
    public Observable<GetBuyerGraph> getBuyerGraph(long startDate, long endDate) {
        String shopId = shopInfoRepository.getShopId();
        return gmStatDataSource.getBuyerGraph(shopId, startDate, endDate);
    }

    @Override
    public Observable<GetKeyword> getKeywordModel(String categoryId) {
        return gmStatDataSource.getKeywordModel(categoryId);
    }

    @Override
    public Observable<GetShopCategory> getShopCategory(long startDate, long endDate) {
        String shopId = shopInfoRepository.getShopId();
        return gmStatDataSource.getShopCategory(shopId, startDate, endDate);
    }

    @Override
    public Observable<Boolean> clearCache() {
        return gmStatDataSource.clearAllCache();
    }

    @Override
    public Observable<GetProductTable> getProductTable(long startDate, long endDate) {
        String shopId = shopInfoRepository.getShopId();
        return gmStatDataSource.getProductTable(shopId, startDate, endDate);
    }

    @Override
    public Observable<GetBuyerTable> getBuyerTable(long startDate, long endDate) {
        String shopId = shopInfoRepository.getShopId();
        return gmStatDataSource.getBuyerTable(shopId, startDate, endDate);
    }


}
