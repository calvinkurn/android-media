package com.tokopedia.home.beranda.data.repository

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.datasource.local.HomeCachedDataSource
import com.tokopedia.home.beranda.data.datasource.remote.*
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import dagger.Lazy
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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
        private val homeDynamicChannelDataMapper: HomeDynamicChannelDataMapper,
        private val applicationContext: Context?,
        private val remoteConfig: RemoteConfig
): HomeRepository {

    var CHANNEL_LIMIT_FOR_PAGINATION = 1
    var isCacheExist = false
    private val queryHashingKey = "android_do_query_hashing"

    override fun getHomeData(): Flow<HomeData?> = homeCachedDataSource.getCachedHomeData().map {
        isCacheExist = it != null
        it
    }

    override fun updateHomeData(): Flow<Result<Any>> = flow{
        coroutineScope {
            /**
             * Remote config to disable pagination by request with param 0
             */
            if (!remoteConfig.getBoolean(RemoteConfigKey.HOME_ENABLE_PAGINATION)) {
                CHANNEL_LIMIT_FOR_PAGINATION = 0
            }
            val isCacheExistForProcess = isCacheExist
            val currentTimeMillisString = System.currentTimeMillis().toString()
            var currentToken = ""

            val homeDataResponse = async { homeRemoteDataSource.getHomeData() }
            val dynamicChannelResponse = async { homeRemoteDataSource.getDynamicChannelData(numOfChannel = CHANNEL_LIMIT_FOR_PAGINATION, doQueryHash = remoteConfig.getBoolean(queryHashingKey, false)) }

            var homeDataCombined: HomeData? = HomeData()

            val homeDataResponseValue = try {
                homeDataResponse.await()
            } catch (e: Exception) {
                HomeData()
            }

            val dynamicChannelResponseValue = try {
                dynamicChannelResponse.await()
            } catch (e: Exception) {
                if (e is SocketTimeoutException && !isCacheExist) {
                    null
                } else {
                    HomeChannelData()
                }
            }

            homeDataResponseValue?.banner?.timestamp = currentTimeMillisString

            /**
             * Proceed to pagination mechanism if cache is not exist
             */
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

                if (homeDataResponseValue == null) {
                    homeCachedDataSource.saveToDatabase(homeDefaultDataSource.getDefaultHomeData())
                } else {
                    homeCachedDataSource.saveToDatabase(homeDataResponseValue)
                }
            } else if (dynamicChannelResponseValue == null) {
                emit(Result.error(Throwable()))
                return@coroutineScope
            }

            /**
             * Proceed to full request dynamic channel
             * - if there is token and cache is not exist
             * - if cache is exist
             *
             */
            if ((!isCacheExistForProcess && currentToken.isNotEmpty()) ||
                    isCacheExistForProcess) {
                try {
                    homeDataCombined = processFullPageDynamicChannel(homeDataResponseValue)
                    homeDataCombined?.dynamicHomeChannel?.channels?.forEach {
                        it.timestamp = currentTimeMillisString
                    }
                    homeDataCombined?.let {
                        emit(Result.success(null))
                        homeCachedDataSource.saveToDatabase(it)
                    }
                } catch (e: Exception) {
                    emit(Result.errorPagination(e,null))
                    return@coroutineScope
                }
            }
        }
    }

    override suspend fun onDynamicChannelExpired(groupId: String): List<Visitable<*>> {
        val dynamicChannelResponse = homeRemoteDataSource.getDynamicChannelData(groupIds = groupId,doQueryHash = remoteConfig.getBoolean(queryHashingKey, false))
        val homeChannelData = HomeChannelData(dynamicChannelResponse.dynamicHomeChannel)

        return homeDynamicChannelDataMapper.mapToDynamicChannelDataModel(
                homeChannelData,
                isCache = false,
                addLoadingMore = false,
                useDefaultWhenEmpty = false)
    }

    private suspend fun processFullPageDynamicChannel(homeDataResponse: HomeData?): HomeData? {
        var dynamicChannelCompleteResponse = homeRemoteDataSource.getDynamicChannelData(numOfChannel = 0, token = homeDataResponse?.token?:"",doQueryHash = remoteConfig.getBoolean(queryHashingKey, false))
        val currentChannelList = homeDataResponse?.dynamicHomeChannel?.channels?.toMutableList()?: mutableListOf()
        currentChannelList.addAll(dynamicChannelCompleteResponse.dynamicHomeChannel.channels)

        homeDataResponse?.let {
            homeDataResponse.token = ""
            homeDataResponse.dynamicHomeChannel.channels = currentChannelList.toList()
        }
        return homeDataResponse
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