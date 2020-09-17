package com.tokopedia.home.beranda.data.repository

import android.util.Log
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.datasource.local.HomeCachedDataSource
import com.tokopedia.home.beranda.data.datasource.remote.*
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelDataModel
import dagger.Lazy
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import rx.Observable
import java.net.SocketTimeoutException
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
        private val homeCachedDataSource: HomeCachedDataSource,
        private val homeRemoteDataSource: HomeRemoteDataSource,
        private val homeDefaultDataSource: HomeDefaultDataSource,
        private val geolocationRemoteDataSource: Lazy<GeolocationRemoteDataSource>,
        private val homeDynamicChannelDataMapper: HomeDynamicChannelDataMapper
): HomeRepository {

    var isCacheExist = false

    override fun getHomeData(): Flow<HomeData?> = homeCachedDataSource.getCachedHomeData().map {
        isCacheExist = it != null
        it
    }

    override fun updateHomeData(): Flow<Result<Any>> = flow{
        val startMillis = System.currentTimeMillis()
        coroutineScope {
            val isCacheExistForProcess = isCacheExist
            val currentTimeMillisString = System.currentTimeMillis().toString()
            var currentToken = ""

            val homeDataResponse = async { homeRemoteDataSource.getHomeData() }
            var dynamicChannelResponse = async { homeRemoteDataSource.getDynamicChannelData(numOfChannel = 2) }

            var homeDataCombined: HomeData? = HomeData()

            Log.d("FikryDebug","FirstAsync: "+(System.currentTimeMillis()-startMillis)+" ms")
            val homeDataResponseValue = try {
                homeDataResponse.await()
            } catch (e: Exception) {
                HomeData()
            }
            Log.d("FikryDebug","HomeDataAwait: "+(System.currentTimeMillis()-startMillis)+" ms")

            val dynamicChannelResponseValue = try {
                dynamicChannelResponse.await()
            } catch (e: Exception) {
                if (e is SocketTimeoutException && !isCacheExist) {
                    null
                } else {
                    HomeChannelData()
                }
            }
            Log.d("FikryDebug","DynamicChannelAwait: "+(System.currentTimeMillis()-startMillis)+" ms")

            if (!isCacheExistForProcess && dynamicChannelResponseValue != null) {
                val extractPair = extractToken(dynamicChannelResponseValue)

                homeDataResponseValue?.let {
                    it.dynamicHomeChannel = extractPair.second.dynamicHomeChannel
                    it.token = extractPair.first
                    it.dynamicHomeChannel.channels.forEach { channel ->
                        channel.timestamp = currentTimeMillisString
                    }
                    currentToken = it.token
                }

                if (homeDataResponse == null) {
                    homeCachedDataSource.saveToDatabase(homeDefaultDataSource.getDefaultHomeData())
                } else {
                    homeCachedDataSource.saveToDatabase(homeDataResponseValue)
                    Log.d("FikryDebug","SaveDatabaseFirst: "+(System.currentTimeMillis()-startMillis)+" ms")
                }
            } else if (dynamicChannelResponseValue == null) {
                emit(Result.error(Throwable()))
                return@coroutineScope
            }

            if ((!isCacheExistForProcess && currentToken.isNotEmpty()) ||
                    isCacheExistForProcess) {
                try {
                    homeDataCombined = processFullPageDynamicChannel(homeDataResponseValue)
                } catch (e: Exception) {
                    emit(Result.errorPagination(e,null))
                    return@coroutineScope
                }
            }

            homeDataCombined?.dynamicHomeChannel?.channels?.forEach {
                it.timestamp = currentTimeMillisString
            }
            homeDataCombined?.let {
                emit(Result.success(null))
                homeCachedDataSource.saveToDatabase(it)
                Log.d("FikryDebug","SaveDatabaseFull: "+(System.currentTimeMillis()-startMillis)+" ms")
            }
        }
        Log.d("FikryDebug","End: "+(System.currentTimeMillis()-startMillis)+" ms")
    }

    override suspend fun onDynamicChannelExpired(groupId: String): List<Visitable<*>> {
        val dynamicChannelResponse = homeRemoteDataSource.getDynamicChannelData(groupIds = groupId)
        val homeChannelData = HomeChannelData(dynamicChannelResponse.dynamicHomeChannel)

        return homeDynamicChannelDataMapper.mapToDynamicChannelDataModel(
                homeChannelData,
                isCache = false,
                addLoadingMore = false,
                useDefaultWhenEmpty = false)
    }

    private suspend fun processFullPageDynamicChannel(homeDataResponse: HomeData?): HomeData? {
        var dynamicChannelCompleteResponse = homeRemoteDataSource.getDynamicChannelData(numOfChannel = 0, token = homeDataResponse?.token?:"")
        val currentChannelList = homeDataResponse?.dynamicHomeChannel?.channels?.toMutableList()?: mutableListOf()
        currentChannelList.addAll(dynamicChannelCompleteResponse.dynamicHomeChannel.channels)

        homeDataResponse?.let {
            homeDataResponse.token = ""
            homeDataResponse.dynamicHomeChannel.channels = currentChannelList.toList()
        }
        return homeDataResponse
    }

    private suspend fun processFirstPageDynamicChannel(dynamicChannelResponse: Deferred<HomeChannelData>, homeDataResponse: Deferred<HomeData>, currentToken: String): String {
        var currentToken1 = currentToken
        val extractPair = extractToken(dynamicChannelResponse.await())

        val homeDataResponseValue = homeDataResponse.await()
        homeDataResponseValue.dynamicHomeChannel = extractPair.second.dynamicHomeChannel
        homeDataResponseValue.token = extractPair.first
        currentToken1 = homeDataResponseValue.token
        if (homeDataResponse == null) {
            homeCachedDataSource.saveToDatabase(homeDefaultDataSource.getDefaultHomeData())
        } else {
            homeCachedDataSource.saveToDatabase(homeDataResponseValue)
        }
        return currentToken1
    }

    override fun sendGeolocationInfo(): Observable<Response<String>> = geolocationRemoteDataSource.get().sendGeolocationInfo()

    override fun deleteHomeData() {
        homeCachedDataSource.deleteHomeData()
    }

    private fun extractToken(homeChannelData: HomeChannelData): Pair<String, HomeChannelData> {
        return if (homeChannelData.dynamicHomeChannel.channels.isNotEmpty()) {
            val token = homeChannelData.dynamicHomeChannel.channels[0].token
            homeChannelData.dynamicHomeChannel.channels[0].token = ""
            Pair(token, homeChannelData)
        } else Pair("", homeChannelData)
    }
}