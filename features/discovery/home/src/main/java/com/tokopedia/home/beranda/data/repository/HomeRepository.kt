package com.tokopedia.home.beranda.data.repository

import androidx.lifecycle.LiveData
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.helper.Resource
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeViewModel
import retrofit2.Response
import rx.Observable

interface HomeRepository {
//    val allHomeData: Observable<HomeViewModel?>
//    val homeDataCache: Observable<HomeViewModel?>
    suspend fun getHomeData(): LiveData<Resource<HomeViewModel>>
    fun sendGeolocationInfo(): Observable<Response<String>>
}

