package com.tokopedia.tkpd.beranda.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.tkpd.beranda.data.source.BrandsOfficialStoreDataSource;
import com.tokopedia.tkpd.beranda.data.source.HomeBannerDataSource;
import com.tokopedia.tkpd.beranda.data.source.HomeCategoryDataSource;
import com.tokopedia.tkpd.beranda.data.source.TickerDataSource;
import com.tokopedia.tkpd.beranda.data.source.TopPicksDataSource;
import com.tokopedia.tkpd.beranda.domain.model.banner.HomeBannerResponseModel;
import com.tokopedia.tkpd.beranda.domain.model.brands.BrandsOfficialStoreResponseModel;
import com.tokopedia.tkpd.beranda.domain.model.category.HomeCategoryResponseModel;
import com.tokopedia.tkpd.beranda.domain.model.toppicks.TopPicksResponseModel;
import rx.Observable;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomeRepositoryImpl implements HomeRepository {

    private final HomeCategoryDataSource categoryDataSource;
    private final HomeBannerDataSource homeBannerDataSource;
    private final BrandsOfficialStoreDataSource brandsOfficialStoreDataSource;
    private final TopPicksDataSource topPicksDataSource;
    private final TickerDataSource tickerDataSource;


    public HomeRepositoryImpl(HomeCategoryDataSource categoryDataSource, HomeBannerDataSource homeBannerDataSource,
                              BrandsOfficialStoreDataSource brandsOfficialStoreDataSource,
                              TopPicksDataSource topPicksDataSource,
                              TickerDataSource tickerDataSource) {
        this.categoryDataSource = categoryDataSource;
        this.homeBannerDataSource = homeBannerDataSource;
        this.brandsOfficialStoreDataSource = brandsOfficialStoreDataSource;
        this.topPicksDataSource = topPicksDataSource;
        this.tickerDataSource = tickerDataSource;
    }

    @Override
    public Observable<HomeCategoryResponseModel> getHomeCategorys() {
        return categoryDataSource.getHomeCategory();
    }

    @Override
    public Observable<TopPicksResponseModel> getTopPicks(RequestParams requestParams) {
        return topPicksDataSource.getTopPicks(requestParams);
    }

    @Override
    public Observable<HomeBannerResponseModel> getBanners(RequestParams requestParams) {
        return homeBannerDataSource.getHomeBanner(requestParams);
    }

    @Override
    public Observable<BrandsOfficialStoreResponseModel> getBrandsOfficialStore() {
        return brandsOfficialStoreDataSource.getBrandsOfficialStore();
    }

    @Override
    public Observable<Ticker> getTickers() {
        return tickerDataSource.getTicker();
    }

    @Override
    public Observable<HomeCategoryResponseModel> getHomeCategorysCache() {
        return categoryDataSource.getCache();
    }

    @Override
    public Observable<TopPicksResponseModel> getTopPicksCache() {
        return topPicksDataSource.getCache();
    }

    @Override
    public Observable<HomeBannerResponseModel> getBannersCache() {
        return homeBannerDataSource.getCache();
    }

    @Override
    public Observable<BrandsOfficialStoreResponseModel> getBrandsOfficialStoreCache() {
        return brandsOfficialStoreDataSource.getCache();
    }

    @Override
    public Observable<Ticker> getTickersCache() {
        return tickerDataSource.getCache();
    }
}
