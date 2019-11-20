package com.tokopedia.home.beranda.data.repository

import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeViewModel
import retrofit2.Response
import rx.Observable

interface HomeRepository {
    val allHomeData: Observable<HomeViewModel?>
    val homeDataCache: Observable<HomeViewModel?>
    fun sendGeolocationInfo(): Observable<Response<String>>
}

