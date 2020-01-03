package com.tokopedia.home.beranda.data.repository

import androidx.lifecycle.LiveData
import com.tokopedia.home.beranda.data.model.PlayLiveDynamicChannelEntity
import com.tokopedia.home.beranda.helper.Resource
import com.tokopedia.home.beranda.domain.model.HomeData
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import rx.Observable

interface HomeRepository {
    fun getHomeData(): LiveData<HomeData?>
    suspend fun updateHomeData(): Resource<Any>
    fun sendGeolocationInfo(): Observable<Response<String>>
    fun getPlayChannel(): Flow<PlayLiveDynamicChannelEntity>
}

