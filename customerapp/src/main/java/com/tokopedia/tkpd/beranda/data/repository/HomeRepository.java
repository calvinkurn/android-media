package com.tokopedia.tkpd.beranda.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.tkpd.beranda.domain.model.banner.HomeBannerResponseModel;
import com.tokopedia.tkpd.beranda.domain.model.brands.BrandsOfficialStoreResponseModel;
import com.tokopedia.tkpd.beranda.domain.model.category.HomeCategoryResponseModel;
import com.tokopedia.tkpd.beranda.domain.model.toppicks.TopPicksResponseModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.SaldoViewModel;

import rx.Observable;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public interface HomeRepository {

    Observable<HomeCategoryResponseModel> getHomeCategorys();

    Observable<HomeCategoryResponseModel> getHomeCategorysCache();

    Observable<TopPicksResponseModel> getTopPicks(RequestParams requestParams);

    Observable<TopPicksResponseModel> getTopPicksCache();

    Observable<HomeBannerResponseModel> getBanners(RequestParams requestParams);

    Observable<HomeBannerResponseModel> getBannersCache();

    Observable<BrandsOfficialStoreResponseModel> getBrandsOfficialStore();

    Observable<BrandsOfficialStoreResponseModel> getBrandsOfficialStoreCache();

    Observable<Ticker> getTickers();

    Observable<Ticker> getTickersCache();

}
