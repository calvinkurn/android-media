package com.tokopedia.home.beranda.data.repository;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.home.beranda.data.source.HomeDataSource;
import java.util.List;

import rx.Observable;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomeRepositoryImpl implements HomeRepository {

    private final HomeDataSource homeDataSource;


    public HomeRepositoryImpl(HomeDataSource homeDataSource) {
        this.homeDataSource = homeDataSource;
    }

    @Override
    public Observable<List<Visitable>> getAllHomeData() {
        return homeDataSource.getHomeData();
    }

    @Override
    public Observable<List<Visitable>> getHomeDataCache() {
        return homeDataSource.getCache();
    }
}
