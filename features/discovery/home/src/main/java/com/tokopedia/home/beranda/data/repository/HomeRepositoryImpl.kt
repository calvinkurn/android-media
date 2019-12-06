package com.tokopedia.home.beranda.data.repository

import android.os.SystemClock
import android.text.TextUtils
import androidx.lifecycle.LiveData
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.home.beranda.data.datasource.local.dao.HomeDao
import com.tokopedia.home.beranda.data.datasource.remote.HomeRemoteDataSource
import com.tokopedia.home.beranda.data.source.HomeDataSource
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.HomeRoomData
import com.tokopedia.home.beranda.helper.Resource
import com.tokopedia.home.beranda.helper.map
import retrofit2.Response
import rx.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
        private val homeDataSource: HomeDataSource,
        private val homeDao: HomeDao,
        private val homeRemoteDataSource: HomeRemoteDataSource
): HomeRepository {

    private val timeout = TimeUnit.SECONDS.toMillis(10)
    override fun getHomeData(): LiveData<HomeData?> {
        return homeDao.getHomeData().map {
            if(SystemClock.uptimeMillis() - (it?.modificationDate?.time ?: SystemClock.uptimeMillis()) > timeout){
                homeDao.deleteHomeData()
                null
            } else {
                it?.homeData
            }
        }
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
        homeDao.save(HomeRoomData(homeData = homeData))
        return Resource.success(homeData)
    }

    override fun sendGeolocationInfo(): Observable<Response<String>> = homeDataSource.sendGeolocationInfo()
}