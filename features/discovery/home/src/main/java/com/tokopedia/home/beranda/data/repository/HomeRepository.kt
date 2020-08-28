package com.tokopedia.home.beranda.data.repository

import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.helper.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import rx.Observable

interface HomeRepository {
    fun getHomeData(): Flow<HomeData?>
    fun updateHomeData(): Flow<Result<Any>>
    fun sendGeolocationInfo(): Observable<Response<String>>
    fun deleteHomeData()
}

