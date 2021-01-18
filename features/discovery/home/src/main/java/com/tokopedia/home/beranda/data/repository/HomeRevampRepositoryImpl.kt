package com.tokopedia.home.beranda.data.repository

import android.content.Context
import com.google.android.exoplayer2.util.Log
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.datasource.local.HomeCachedDataSource
import com.tokopedia.home.beranda.data.datasource.remote.*
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.data.model.HomeAtfData
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home.constant.AtfKey.TYPE_BANNER
import com.tokopedia.home.constant.AtfKey.TYPE_CHANNEL
import com.tokopedia.home.constant.AtfKey.TYPE_ICON
import com.tokopedia.home.constant.AtfKey.TYPE_TICKER
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import dagger.Lazy
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.selects.select
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

    val jobList = mutableListOf<Deferred<Unit?>>()

    override fun getHomeData(): Flow<HomeData?> = homeCachedDataSource.getCachedHomeData().map {
        isCacheExist = it != null
        it
    }

    /**
     * Home repository flow:
     *
     * 1. Provide initial HomeData
     * 2. Get above the fold skeleton
     * 3. Save immediately to produce shimmering for ATF data
     * 4. Get above the fold content
     * 5. Submit current data to database, to trigger HomeViewModel flow
     *      if there is no cache, then submit immediately
     *      if cache exist, don't submit to database because it will trigger jumpy experience
     * 6. Get dynamic channel data
     *    6.1. If channel cache is empty, proceed to channel pagination
     *    6.2. If channel cache is not empty, proceed to full channel request
     *      if there is token and cache is not exist
     *      if cache is exist
     * 7. Submit current data to database, to trigger HomeViewModel flow
     *    7.1 Emit error pagination only when atf is empty
     *      Because there is no content that we can show, we showing error page
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
            try {
                val homeAtfResponse = async { homeRemoteDataSource.getAtfDataUseCase() }.await()
                homeData.atfData = homeAtfResponse
            } catch (e: Exception) {
                homeData.atfData = null
            }

            /**
             * 3. Save immediately to produce shimmering for ATF data
             */
            cacheCondition(isCache = isCacheExistForProcess, isCacheEmptyAction = {
                homeCachedDataSource.saveToDatabase(homeData)
            })

            homeData.atfData?.isProcessingAtf
            /**
             * 4. Get above the fold content
             */
            if (homeData?.atfData?.dataList?.isNotEmpty() == true) {
                homeData.atfData?.dataList?.map { atfData ->
                    when(atfData.component) {
                        TYPE_TICKER -> {
                            val job = async {
                                try {
                                    val ticker = homeRemoteDataSource.getHomeTickerUseCase()
                                    ticker?.let {
                                        atfData.content = gson.toJson(ticker.ticker)
                                        atfData.status = AtfKey.STATUS_SUCCESS
                                        cacheCondition(isCache = isCacheExistForProcess, isCacheEmptyAction = {
                                            homeCachedDataSource.saveToDatabase(homeData)
                                        })
                                    }
                                } catch (e: Exception) {
                                    atfData.status = AtfKey.STATUS_ERROR
                                    cacheCondition(isCache = isCacheExistForProcess, isCacheEmptyAction = {
                                        homeCachedDataSource.saveToDatabase(homeData)
                                    })
                                }
                            }
                            jobList.add(job)
                        }
                        TYPE_CHANNEL -> {
                            val job = async {
                                try {
                                    val dynamicChannel = homeRemoteDataSource.getDynamicChannelData(params = atfData.param)
                                    dynamicChannel.let {
                                        val channelFromResponse = it.dynamicHomeChannel
                                        atfData.content = gson.toJson(channelFromResponse)
                                        atfData.status = AtfKey.STATUS_SUCCESS
                                        cacheCondition(isCache = isCacheExistForProcess, isCacheEmptyAction = {
                                            homeCachedDataSource.saveToDatabase(homeData)
                                        })
                                    }
                                } catch (e: Exception) {
                                    atfData.status = AtfKey.STATUS_ERROR
                                    atfData.content = null
                                    cacheCondition(isCache = isCacheExistForProcess, isCacheEmptyAction = {
                                        homeCachedDataSource.saveToDatabase(homeData)
                                    })
                                }
                            }
                            jobList.add(job)
                        }
                        TYPE_ICON -> {
                            val job = async {
                                try {
                                    val dynamicIcon = homeRemoteDataSource.getHomeIconUseCase(atfData.param)
                                    dynamicIcon?.let {
                                        atfData.content = gson.toJson(dynamicIcon.dynamicHomeIcon)
                                        atfData.status = AtfKey.STATUS_SUCCESS
                                        cacheCondition(isCache = isCacheExistForProcess, isCacheEmptyAction = {
                                            homeCachedDataSource.saveToDatabase(homeData)
                                        })
                                    }
                                } catch (e: Exception) {
                                    atfData.status = AtfKey.STATUS_ERROR
                                    cacheCondition(isCache = isCacheExistForProcess, isCacheEmptyAction = {
                                        homeCachedDataSource.saveToDatabase(homeData)
                                    })
                                }
                            }
                            jobList.add(job)
                        }
                        else -> {

                        }
                    }
                }
                /**
                 * this is to filter the first deffered finished
                 *
                 * 5. Submit current data to database, to trigger HomeViewModel flow
                 * if there is no cache, then submit immediately
                 * if cache exist, don't submit to database because it will trigger jumpy experience
                 */
                select<Unit?> {
                    jobList.forEach {
                        it.onAwait {
                            cacheCondition(isCache = isCacheExistForProcess, isCacheEmptyAction = {
                                homeCachedDataSource.saveToDatabase(homeData)
                            })
                        }
                    }
                }
            }

            homeData.atfData?.isProcessingAtf = false
            /**
             * 6. Get dynamic channel data
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
             * 6.1. If channel cache is empty, proceed to channel pagination
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
             * 6.2. If channel cache is not empty, proceed to full channel request
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
                         * 7. Submit current data to database, to trigger HomeViewModel flow
                         */
                        homeCachedDataSource.saveToDatabase(it)
                    }
                } catch (e: Exception) {
                    /**
                     * 7.1 Emit error pagination only when atf is empty
                     * Because there is no content that we can show, we showing error page
                     */
                    if (homeData.atfData?.dataList == null || homeData.atfData?.dataList?.isEmpty() == true) {
                        emit(Result.errorPagination(e,null))
                    }
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

    private suspend fun cacheCondition(
            isCache: Boolean,
            isCacheExistAction: suspend () -> Unit = {}, isCacheEmptyAction: suspend () -> Unit = {}) {
        if (isCache) {
            isCacheExistAction.invoke()
        } else {
            isCacheEmptyAction.invoke()
        }
    }
}