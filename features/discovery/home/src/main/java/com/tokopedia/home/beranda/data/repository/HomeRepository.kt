package com.tokopedia.home.beranda.data.repository

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.data.source.Resource
import com.tokopedia.home.beranda.domain.model.HomeData
import retrofit2.Response
import rx.Observable

interface HomeRepository {
    suspend fun getHomeData(): LiveData<Resource<HomeData>>
    fun sendGeolocationInfo(): Observable<Response<String>>
}

