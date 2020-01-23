package com.tokopedia.home.beranda.data.repository

import android.text.TextUtils
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.home.beranda.data.datasource.HomeCachedDataSource
import com.tokopedia.home.beranda.data.datasource.remote.HomeRemoteDataSource
import com.tokopedia.home.beranda.data.source.HomeDataSource
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.helper.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import rx.Observable
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
        private val homeDataSource: HomeDataSource,
        private val homeCachedDataSource: HomeCachedDataSource,
        private val homeRemoteDataSource: HomeRemoteDataSource
): HomeRepository {

    override suspend fun getHomeData(): Flow<HomeData?> {
        return homeCachedDataSource.getCachedHomeData()
    }

    override suspend fun updateHomeData(): Resource<Any> {
        val response = homeRemoteDataSource.getHomeData()
        response.getError(HomeData::class.java)?.let {
            if (it.isNotEmpty()) {
                if (!TextUtils.isEmpty(it[0].message)){
                    return Resource.error(Throwable(it[0].message))
                }
            }
        }
        val homeData = response.getSuccessData<HomeData>()
        homeCachedDataSource.saveToDatabase(homeData)
        return Resource.success(homeData)
    }

    override fun sendGeolocationInfo(): Observable<Response<String>> = homeDataSource.sendGeolocationInfo()
}