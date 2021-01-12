package com.tokopedia.home.beranda.data.repository

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.datasource.local.HomeCachedDataSource
import com.tokopedia.home.beranda.data.datasource.remote.*
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.constant.AtfKey.TYPE_BANNER
import com.tokopedia.home.constant.AtfKey.TYPE_CHANNEL
import com.tokopedia.home.constant.AtfKey.TYPE_ICON
import com.tokopedia.home.constant.AtfKey.TYPE_TICKER
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

/**
 * Home repository class used to provide the data from Home Services
 *
 * - Above the fold data
 * - Dynamic Channel
 * - Slider Banner (HPB)
 * - Icon
 * - Ticker
 *
 */
class HomeRevampRepositoryImpl @Inject constructor(
        private val homeCachedDataSource: HomeCachedDataSource,
        private val homeRemoteDataSource: HomeRemoteDataSource,
        private val homeDefaultDataSource: HomeDefaultDataSource,
        private val geolocationRemoteDataSource: Lazy<GeolocationRemoteDataSource>,
        private val homeDynamicChannelDataMapper: HomeDynamicChannelDataMapper,
        private val applicationContext: Context?,
        private val remoteConfig: RemoteConfig
): HomeRevampRepository {

    var CHANNEL_LIMIT_FOR_PAGINATION = 1
    var isCacheExist = false
    val gson = Gson()

    override fun getHomeData(): Flow<HomeData?> = homeCachedDataSource.getCachedHomeData().map {
        isCacheExist = it != null
        it
    }

    /**
     * Home repository flow:
     *
     * 1.   Provide initial HomeData
     * 2.   Get above the fold skeleton
     * 3.   Get above the fold content
     * 4.   Submit current data to database, to trigger HomeViewModel flow
     * 5.   Get dynamic channel data
     * 5.1. If channel cache is empty, proceed to channel pagination
     * 5.2. If channel cache is not empty, proceed to full channel request
     * 6.   Submit current data to database, to trigger HomeViewModel flow
     */
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

            /**
             * 1. Provide initial HomeData
             */
            var homeData = HomeData()

            /**
             * 2. Get above the fold skeleton
             */
            val homeAtfResponse = async { homeRemoteDataSource.getAtfDataUseCase() }.await()
            homeData.atfData = homeAtfResponse

            /**
             * 3. Get above the fold content
             */
            homeData.atfData?.dataList?.map { atfData ->
                when(atfData.component) {
                    TYPE_TICKER -> {
                        val ticker = homeRemoteDataSource.getHomeTickerUseCase()
                        ticker?.let { atfData.content = gson.toJson(ticker.ticker) }
                    }
                    TYPE_BANNER -> {
//                        val banner = homeRemoteDataSource.getHomePageBannerUseCase()
//                        banner?.let { homeData.banner = banner.banner }
                    }
                    TYPE_CHANNEL -> {
                        val dynamicChannel = homeRemoteDataSource.getDynamicChannelData(params = atfData.param)
                        dynamicChannel.let {
                            val channelFromResponse = it.dynamicHomeChannel
                            /**
                             * 3.1 Combine the channel
                             */
                            atfData.content = gson.toJson(channelFromResponse)
                        }
                    }
                    TYPE_ICON -> {
                        val dynamicIcon = homeRemoteDataSource.getHomeIconUseCase()
                        dynamicIcon?.let { atfData.content = gson.toJson(dynamicIcon.dynamicHomeIcon) }
                    }
                    else -> {

                    }
                }
            }

            /**
             * 4. Submit current data to database, to trigger HomeViewModel flow
             * if there is no cache, then submit immediately
             * if cache exist, don't submit to database because it will trigger jumpy experience
             */
            if (!isCacheExist) {
                homeCachedDataSource.saveToDatabase(homeData)
            }

            /**
             * 5.   Get dynamic channel data
             */
            val dynamicChannelResponse = async { homeRemoteDataSource.getDynamicChannelData(numOfChannel = CHANNEL_LIMIT_FOR_PAGINATION) }
            val dynamicChannelResponseValue = try {
                dynamicChannelResponse.await()
            } catch (e: Exception) {
                if (e is SocketTimeoutException && !isCacheExist) {
                    null
                } else {
                    HomeChannelData()
                }
            }

            /**
             * 5.1. If channel cache is empty, proceed to channel pagination
             */
            if (!isCacheExistForProcess && dynamicChannelResponseValue != null) {
                val extractPair = extractToken(dynamicChannelResponseValue)

                homeData.let {
                    val combinedChannel = combineChannelWith(it.dynamicHomeChannel, extractPair.second.dynamicHomeChannel)
                    it.dynamicHomeChannel = combinedChannel
                    it.token = extractPair.first
                    it.dynamicHomeChannel.channels.forEach { channel ->
                        channel.timestamp = currentTimeMillisString
                    }
                    currentToken = it.token
                }

                homeCachedDataSource.saveToDatabase(homeData)
            } else if (dynamicChannelResponseValue == null) {
                emit(Result.error(Throwable()))
                return@coroutineScope
            }

            /**
             * 5.2. If channel cache is not empty, proceed to full channel request
             * Proceed to full request dynamic channel
             * - if there is token and cache is not exist
             * - if cache is exist
             *
             */
            if ((!isCacheExistForProcess && currentToken.isNotEmpty()) ||
                    isCacheExistForProcess) {
                try {
                    homeData = processFullPageDynamicChannel(homeData)?: HomeData()
                    homeData.dynamicHomeChannel.channels.forEach {
                        it.timestamp = currentTimeMillisString
                    }
                    homeData.let {
                        emit(Result.success(null))
                        /**
                         * 6. Submit current data to database, to trigger HomeViewModel flow
                         */
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
            val combinedChannel = combineChannelWith(homeDataResponse.dynamicHomeChannel, dynamicChannelCompleteResponse.dynamicHomeChannel)
            homeDataResponse.dynamicHomeChannel = combinedChannel
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

    private fun combineChannelWith(currentChannel: DynamicHomeChannel, newChannel: DynamicHomeChannel): DynamicHomeChannel {
        val combinationChannel = currentChannel.channels.toMutableList()
        combinationChannel.addAll(newChannel.channels)
        return DynamicHomeChannel(combinationChannel)
    }
}