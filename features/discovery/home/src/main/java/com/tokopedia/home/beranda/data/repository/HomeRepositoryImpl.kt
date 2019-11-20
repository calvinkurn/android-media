package com.tokopedia.home.beranda.data.repository

import com.tokopedia.home.beranda.data.source.HomeDataSource
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeViewModel
import retrofit2.Response
import rx.Observable
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
        private val homeDataSource: HomeDataSource
): HomeRepository {

    override val allHomeData: Observable<HomeViewModel?> = homeDataSource.homeData

    override val homeDataCache: Observable<HomeViewModel?> = homeDataSource.cache

    override fun sendGeolocationInfo(): Observable<Response<String>> = homeDataSource.sendGeolocationInfo()
}