package com.tokopedia.home.beranda.data.repository

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.helper.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import rx.Observable

interface HomeRepository {
//    fun getHomeCachedData(): HomeData?
    fun getHomeData(): Flow<HomeData?>
    fun updateHomeData(): Flow<Result<Any>>
    suspend fun onDynamicChannelExpired(groupId: String): List<Visitable<*>>
    fun sendGeolocationInfo(): Observable<Response<String>>
    fun deleteHomeData()
}

