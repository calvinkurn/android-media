package com.tokopedia.home.beranda.data.repository;

import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeViewModel;

import java.util.List;

import retrofit2.Response;
import rx.Observable;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public interface HomeRepository {

    Observable<HomeViewModel> getAllHomeData();

    Observable<HomeViewModel> getHomeDataCache();

    Observable<Response<String>> sendGeolocationInfo();
}
