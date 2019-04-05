package com.tokopedia.home.beranda.data.repository;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.beranda.data.source.HomeDataSource;
import com.tokopedia.home.beranda.presentation.view.adapter.TrackedVisitable;

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
    public Observable<List<TrackedVisitable>> getAllHomeData() {
        return homeDataSource.getHomeData();
    }

    @Override
    public Observable<List<TrackedVisitable>> getHomeDataCache() {
        return homeDataSource.getCache();
    }
}
