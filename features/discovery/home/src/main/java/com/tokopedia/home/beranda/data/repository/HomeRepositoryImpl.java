package com.tokopedia.home.beranda.data.repository;

import com.tokopedia.home.beranda.data.source.HomeDataSource;
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeViewModel;

import java.util.List;

import retrofit2.Response;
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
    public Observable<HomeViewModel> getAllHomeData() {
        return homeDataSource.getHomeData();
    }

    @Override
    public Observable<HomeViewModel> getHomeDataCache() {
        return homeDataSource.getCache();
    }

    @Override
    public Observable<Response<String>> sendGeolocationInfo() {
        return homeDataSource.sendGeolocationInfo();
    }
}
