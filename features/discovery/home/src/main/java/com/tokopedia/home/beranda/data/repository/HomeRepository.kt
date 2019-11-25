package com.tokopedia.home.beranda.data.repository

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.data.source.Resource
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeViewModel
import retrofit2.Response
import rx.Observable

interface HomeRepository {
    suspend fun getHomeData(): LiveData<Resource<HomeViewModel>>
    fun sendGeolocationInfo(): Observable<Response<String>>
}

