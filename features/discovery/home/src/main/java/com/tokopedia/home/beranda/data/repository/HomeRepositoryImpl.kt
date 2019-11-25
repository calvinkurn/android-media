package com.tokopedia.home.beranda.data.repository

import android.text.TextUtils
import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.data.source.NetworkBoundResource
import com.tokopedia.abstraction.base.data.source.Resource
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home.beranda.data.datasource.local.dao.HomeDao
import com.tokopedia.home.beranda.data.datasource.remote.HomeRemoteDataSource
import com.tokopedia.home.beranda.data.source.HomeDataSource
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.helper.RateLimiter
import retrofit2.Response
import rx.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
        private val homeDataSource: HomeDataSource,
        private val homeDao: HomeDao,
        private val homeRemoteDataSource: HomeRemoteDataSource
): HomeRepository {

    companion object{
        private const val HOME = "home"
    }

    private val repoListRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)

    override suspend fun getHomeData(): LiveData<Resource<HomeData>> {
        return object : NetworkBoundResource<GraphqlResponse, HomeData>(){

            override fun processResponse(response: GraphqlResponse): HomeData {
                response.getError(HomeData::class.java)?.let {
                    if (it.isNotEmpty()) {
                        if (!TextUtils.isEmpty(it[0].message)){
                            throw Throwable(it[0].message)
                        }
                    }
                }
                return response.getSuccessData()
            }

            override suspend fun saveCallResults(items: HomeData) {
                homeDao.save(items)
            }

            override suspend fun loadFromDb(): HomeData? {
                return homeDao.getHomeData().copy(isCache = true)
            }

            override suspend fun createCallAsync(): GraphqlResponse {
                return homeRemoteDataSource.getHomeData()
            }

            override fun shouldFetch(data: HomeData?): Boolean {
                return data == null || repoListRateLimit.shouldFetch(HOME)
            }

            override suspend fun onFetchError() {
                repoListRateLimit.reset(HOME)
            }
        }.build().asLiveData()
    }

    override fun sendGeolocationInfo(): Observable<Response<String>> = homeDataSource.sendGeolocationInfo()
}