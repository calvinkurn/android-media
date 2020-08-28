package com.tokopedia.home.beranda.data.repository

import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.datasource.local.HomeCachedDataSource
import com.tokopedia.home.beranda.data.datasource.remote.*
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.helper.Result
import dagger.Lazy
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import rx.Observable
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
        private val homeCachedDataSource: HomeCachedDataSource,
        private val homeRemoteDataSource: HomeRemoteDataSource,
        private val homeDefaultDataSource: HomeDefaultDataSource,
        private val geolocationRemoteDataSource: Lazy<GeolocationRemoteDataSource>
): HomeRepository {

    var isCacheExist = false

    override fun getHomeData(): Flow<HomeData?> = homeCachedDataSource.getCachedHomeData().map {
        isCacheExist = it != null
        it
    }

    override fun updateHomeData(): Flow<Result<Any>> = flow{
        val isCacheExistForProcess = isCacheExist
        val homeDataResponse = homeRemoteDataSource.getHomeData()
        val currentTimeMillisString = System.currentTimeMillis().toString()
        var currentToken = ""

        var dynamicChannelResponse = homeRemoteDataSource.getDynamicChannelData(numOfChannel = 2)

        if (!isCacheExistForProcess) {
            currentToken = processFirstPageDynamicChannel(dynamicChannelResponse, homeDataResponse, currentToken)
        }

        if ((!isCacheExistForProcess && currentToken.isNotEmpty()) ||
                isCacheExistForProcess) {
            dynamicChannelResponse = processFullPageDynamicChannel(dynamicChannelResponse, homeDataResponse)
        }

        dynamicChannelResponse.dynamicHomeChannel.channels.forEach {
            it.timestamp = currentTimeMillisString
        }
        homeDataResponse.let {
            emit(Result.success(null))
            homeCachedDataSource.saveToDatabase(homeDataResponse)
        }
    }

    private suspend fun processFullPageDynamicChannel(dynamicChannelResponse: HomeChannelData, homeDataResponse: HomeData): HomeChannelData {
        var dynamicChannelResponse1 = dynamicChannelResponse
        dynamicChannelResponse1 = homeRemoteDataSource.getDynamicChannelData(numOfChannel = 0, token = homeDataResponse.token)
        val currentChannelList = homeDataResponse.dynamicHomeChannel.channels.toMutableList()
        currentChannelList.addAll(dynamicChannelResponse1.dynamicHomeChannel.channels)

        homeDataResponse.token = ""
        homeDataResponse.dynamicHomeChannel.channels = currentChannelList?.toList()
                ?: listOf<DynamicHomeChannel.Channels>()
        return dynamicChannelResponse1
    }

    private suspend fun processFirstPageDynamicChannel(dynamicChannelResponse: HomeChannelData, homeDataResponse: HomeData, currentToken: String): String {
        var currentToken1 = currentToken
        val extractPair = extractToken(dynamicChannelResponse)

        homeDataResponse.dynamicHomeChannel = extractPair.second.dynamicHomeChannel
        homeDataResponse.token = extractPair.first
        currentToken1 = homeDataResponse.token
        if (homeDataResponse == null) {
            homeCachedDataSource.saveToDatabase(homeDefaultDataSource.getDefaultHomeData())
        } else {
            homeCachedDataSource.saveToDatabase(homeDataResponse)
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