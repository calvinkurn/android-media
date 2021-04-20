package com.tokopedia.home.beranda.data.repository

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.datasource.local.HomeCachedDataSource
import com.tokopedia.home.beranda.data.datasource.local.entity.AtfCacheEntity
import com.tokopedia.home.beranda.data.datasource.remote.GeolocationRemoteDataSource
import com.tokopedia.home.beranda.data.datasource.remote.HomeRemoteDataSource
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.data.model.AtfData
import com.tokopedia.home.beranda.data.model.HomeAtfData
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home.constant.AtfKey.TYPE_CHANNEL
import com.tokopedia.home.constant.AtfKey.TYPE_ICON
import com.tokopedia.home.constant.AtfKey.TYPE_TICKER
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils.convertToLocationParams
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import dagger.Lazy
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.Response
import rx.Observable
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

    private var CHANNEL_LIMIT_FOR_PAGINATION = 1
    companion object{
        private const val TYPE_ATF_1 = "atf-1"
    }
    var isCacheExist = false
    val gson = Gson()

    private val jobList = mutableListOf<Deferred<AtfData>>()

    override fun getHomeCachedAtfData(): HomeAtfData? {
        return HomeAtfData(
                dataList = homeCachedDataSource.getCachedAtfData().map {
                    AtfData(
                            id = it.id,
                            name = it.name,
                            component = it.component,
                            param = it.param,
                            isOptional = false,
                            content = it.content,
                            status = AtfKey.STATUS_SUCCESS
                    )
                },
                isProcessingAtf = false
        )
    }

    override fun getHomeData(): Flow<HomeData?> = homeCachedDataSource.getCachedHomeData().map {
        isCacheExist = it != null
        it
    }

    /**
     * Home repository flow:
     *
     * 1. Provide initial HomeData
     * 2. Get above the fold skeleton
     *    2.1 Get home flag response
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
            var isAtfSuccess = true

            /**
             * 1. Provide initial HomeData
             */
            var homeData = HomeData()
            cacheCondition(isCache = isCacheExistForProcess, isCacheEmptyAction = {
                homeData.isProcessingDynamicChannel = true
            }, isCacheExistAction = {
                homeData.isProcessingDynamicChannel = false
            })

            /**
             * 2. Get above the fold skeleton
             */
            try {
                val homeAtfResponse = homeRemoteDataSource.getAtfDataUseCase()
                if (homeAtfResponse?.dataList == null) {
                    isAtfSuccess = false
                    homeData.atfData = null
                } else {
                    homeData.atfData = homeAtfResponse
                }
            } catch (e: Exception) {
                homeData.atfData = null
                isAtfSuccess = false
            }

            /**
             * 2.1 Get home flag response
             */
            try {
                launch {
                    val homeFlagResponse = homeRemoteDataSource.getHomeFlagUseCase()
                    homeFlagResponse?.homeFlag?.let {
                        homeData.homeFlag = homeFlagResponse.homeFlag
                    }
                }
            } catch (e: Exception) {

            }

            /**
             * 4. Get above the fold content
             */
            if (homeData.atfData?.dataList?.isNotEmpty() == true) {
                homeData.atfData?.dataList?.map { atfData ->
                    when(atfData.component) {
                        TYPE_TICKER -> {
                            val job = async {
                                try {
                                    val ticker = homeRemoteDataSource.getHomeTickerUseCase(
                                            locationParams = applicationContext?.let {
                                                ChooseAddressUtils.getLocalizingAddressData(applicationContext)?.convertToLocationParams()} ?: "")
                                    ticker?.let {
                                        atfData.content = gson.toJson(ticker.ticker)
                                        atfData.status = AtfKey.STATUS_SUCCESS
                                    }
                                } catch (e: Exception) {
                                    atfData.status = AtfKey.STATUS_ERROR
                                }
                                atfData
                            }
                            jobList.add(job)
                        }
                        TYPE_CHANNEL -> {
                            val job = async {
                                try {
                                    val dynamicChannel = homeRemoteDataSource.getDynamicChannelData(
                                            params = atfData.param,
                                            locationParams = applicationContext?.let {
                                                ChooseAddressUtils.getLocalizingAddressData(applicationContext)?.convertToLocationParams()} ?: ""
                                    )
                                    dynamicChannel.let {
                                        val channelFromResponse = it.dynamicHomeChannel
                                        atfData.content = gson.toJson(channelFromResponse)
                                        atfData.status = AtfKey.STATUS_SUCCESS
                                    }
                                    homeData.atfData?.isProcessingAtf = false
                                } catch (e: Exception) {
                                    atfData.status = AtfKey.STATUS_ERROR
                                    atfData.content = null
                                }
                                cacheCondition(isCacheExistForProcess, isCacheEmptyAction = {
                                    saveToDatabase(homeData)
                                })
                                atfData
                            }
                            jobList.add(job)
                        }
                        TYPE_ICON -> {
                            val job = async {
                                try {
                                    val dynamicIcon = homeRemoteDataSource.getHomeIconUseCase(
                                            param = atfData.param,
                                            locationParams = applicationContext?.let {
                                                ChooseAddressUtils.getLocalizingAddressData(applicationContext)?.convertToLocationParams()} ?: "")
                                    dynamicIcon.let {
                                        atfData.content = gson.toJson(dynamicIcon.dynamicHomeIcon.copy(type=if(atfData.param.contains(TYPE_ATF_1)) 1 else 2))
                                        atfData.status = AtfKey.STATUS_SUCCESS
                                    }
                                    homeData.atfData?.isProcessingAtf = false
                                } catch (e: Exception) {
                                    atfData.status = AtfKey.STATUS_ERROR
                                }
                                cacheCondition(isCacheExistForProcess, isCacheEmptyAction = {
                                    saveToDatabase(homeData)
                                })
                                atfData
                            }
                            jobList.add(job)
                        }
                        else -> {

                        }
                    }
                }
            }

            /**
             * 6. Get dynamic channel data
             */
            val dynamicChannelResponseValue = try {
                val dynamicChannelResponse = homeRemoteDataSource.getDynamicChannelData(
                        numOfChannel = CHANNEL_LIMIT_FOR_PAGINATION,
                        locationParams = applicationContext?.let {
                            ChooseAddressUtils.getLocalizingAddressData(applicationContext)?.convertToLocationParams()} ?: "")
                if (!isAtfSuccess) {
                    homeData.atfData = null
                }
                dynamicChannelResponse
            } catch (e: Exception) {
                if (!isAtfSuccess && !isCacheExistForProcess) {
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

                homeData.isProcessingDynamicChannel = false
                saveToDatabase(homeData, true)
            } else if (dynamicChannelResponseValue == null) {
                /**
                 * 7.1 Emit error pagination only when atf is empty
                 * Because there is no content that we can show, we showing error page
                 */
                if (homeData.atfData == null ||
                        (homeData.atfData?.dataList == null && homeData.atfData?.isProcessingAtf == false) ||
                        homeData.atfData?.dataList?.isEmpty() == true) {
                    emit(Result.errorGeneral(Throwable(),null))
                } else {
                    emit(Result.error(Throwable(), null))
                }
                saveToDatabase(homeData)
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
                    homeData = processFullPageDynamicChannel(
                            homeDataResponse = homeData)
                            ?: HomeData()
                    homeData.dynamicHomeChannel.channels.forEach {
                        it.timestamp = currentTimeMillisString
                    }
                    homeData.let {
                        emit(Result.success(null))
                        /**
                         * 7. Submit current data to database, to trigger HomeViewModel flow
                         */
                        homeData.isProcessingDynamicChannel = false
                        saveToDatabase(it, true)
                    }
                } catch (e: Exception) {
                    /**
                     * 7.1 Emit error pagination only when atf is empty
                     * Because there is no content that we can show, we showing error page
                     */
                    if (homeData.atfData?.dataList == null || homeData.atfData?.dataList?.isEmpty() == true) {
                        emit(Result.errorPagination(Throwable(),null))
                    }
                    saveToDatabase(homeData)
                }
            }
        }
    }

    private suspend fun saveToDatabase(homeData: HomeData?, saveAtf: Boolean = false) {
        homeCachedDataSource.saveToDatabase(homeData)
        if (saveAtf) {
            homeData?.atfData?.let {
                homeCachedDataSource.saveCachedAtf(
                        it.dataList.map {atfData ->
                            AtfCacheEntity(
                                    id = atfData.id,
                                    name = atfData.name,
                                    component = atfData.component,
                                    param = atfData.param,
                                    isOptional = atfData.isOptional,
                                    content = atfData.content,
                                    status = atfData.status
                            )
                        }
                )
            }
        }
    }

    override suspend fun onDynamicChannelExpired(groupId: String): List<Visitable<*>> {
        val dynamicChannelResponse = homeRemoteDataSource.getDynamicChannelData(
                groupIds = groupId,
                locationParams = applicationContext?.let {
                    ChooseAddressUtils.getLocalizingAddressData(applicationContext)?.convertToLocationParams()} ?: "")
        val homeChannelData = HomeChannelData(dynamicChannelResponse.dynamicHomeChannel)

        return homeDynamicChannelDataMapper.mapToDynamicChannelDataModel(
                homeChannelData,
                isCache = false,
                addLoadingMore = false,
                useDefaultWhenEmpty = false)
    }

    private suspend fun processFullPageDynamicChannel(homeDataResponse: HomeData?): HomeData? {
        var dynamicChannelCompleteResponse = homeRemoteDataSource.getDynamicChannelData(
                numOfChannel = 0,
                token = homeDataResponse?.token?:"",
                locationParams = applicationContext?.let {
                    ChooseAddressUtils.getLocalizingAddressData(applicationContext)?.convertToLocationParams()} ?: "")
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