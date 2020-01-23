package com.tokopedia.home.beranda.data.repository

import android.text.TextUtils
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.home.beranda.data.datasource.HomeCachedDataSource
import com.tokopedia.home.beranda.data.datasource.remote.HomeRemoteDataSource
import com.tokopedia.home.beranda.data.datasource.remote.PlayRemoteDataSource
import com.tokopedia.home.beranda.data.model.PlayLiveDynamicChannelEntity
import com.tokopedia.home.beranda.data.source.HomeDataSource
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.helper.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import rx.Observable
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
        private val homeDataSource: HomeDataSource,
        private val homeCachedDataSource: HomeCachedDataSource,
        private val homeRemoteDataSource: HomeRemoteDataSource,
        private val playRemoteDataSource: PlayRemoteDataSource
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

    override fun getPlayChannel(): Flow<PlayLiveDynamicChannelEntity> = flow {
//        emit(PlayLiveDynamicChannelEntity(PlayDynamicData(PlayData(listOf(PlayChannel(channelId = "1", coverUrl = "https://ecs7.tokopedia.net/img/cache/1400/attachment/2020/1/23/1579775662378/1579775662378_4dbbcd10-9f8c-4b7d-baf2-b1e25becb948.png", videoStream = VideoStream(config = Config(streamUrl = "https://vod.tokopedia.net/73a58b49941d430d949b4a8273efdc74/100779c2d405420da252cc44d4ca21b3-edef9725173feab592c030523316fc60-sd.mp4"))))))))
        emit(playRemoteDataSource.getPlayData(page = 1))
    }
}