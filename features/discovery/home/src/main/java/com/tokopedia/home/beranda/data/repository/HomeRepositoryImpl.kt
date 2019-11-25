package com.tokopedia.home.beranda.data.repository

import android.text.TextUtils
import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.data.source.NetworkBoundResource
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home.beranda.data.datasource.local.dao.HomeDao
import com.tokopedia.home.beranda.data.datasource.remote.HomeRemoteDataSource
import com.tokopedia.home.beranda.data.mapper.HomeMapper
import com.tokopedia.home.beranda.data.source.HomeDataSource
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.abstraction.base.data.source.Resource
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeViewModel
import retrofit2.Response
import rx.Observable
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
        private val homeDataSource: HomeDataSource,
        private val homeDao: HomeDao,
        private val homeRemoteDataSource: HomeRemoteDataSource,
        private val homeMapper: HomeMapper
): HomeRepository {

    override suspend fun getHomeData(): LiveData<Resource<HomeViewModel>> {
        return object : NetworkBoundResource<GraphqlResponse, HomeData, HomeViewModel>(){

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
                val data = homeDao.getHomeData()
                return if(data == null) data
                else data.copy(isCache = true)
            }

            override suspend fun createCallAsync(): GraphqlResponse {
                return homeRemoteDataSource.getHomeData()
            }

            override fun shouldFetch(data: HomeData?): Boolean {
                return true
            }

            override fun mapper(response: HomeData?): HomeViewModel? {
                if(response == null) return null
                return homeMapper.call(response).copy(isCache = response.isCache)
            }

        }.build().asLiveData()
    }

    override fun sendGeolocationInfo(): Observable<Response<String>> = homeDataSource.sendGeolocationInfo()
}