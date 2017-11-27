package com.tokopedia.tkpd.beranda.data.repository;

import com.tokopedia.tkpd.beranda.domain.model.banner.HomeBannerModel;
import com.tokopedia.tkpd.beranda.domain.model.category.HomeCategoryResponseModel;
import com.tokopedia.tkpd.beranda.domain.model.toppicks.TopPicksResponseModel;

import rx.Observable;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomeRepositoryImpl implements HomeRepository {

    @Override
    public Observable<HomeCategoryResponseModel> getHomeCategorys() {
        return null;
    }

    @Override
    public Observable<TopPicksResponseModel> getTopPicks() {
        return null;
    }

    @Override
    public Observable<HomeBannerModel> getBanners() {
        return null;
    }
}
