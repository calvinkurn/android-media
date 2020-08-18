package com.tokopedia.home.beranda.data.repository

import android.text.TextUtils
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.datasource.local.HomeCachedDataSource
import com.tokopedia.home.beranda.data.datasource.remote.*
import com.tokopedia.home.beranda.data.model.KeywordSearchData
import com.tokopedia.home.beranda.data.model.PlayLiveDynamicChannelEntity
import com.tokopedia.home.beranda.data.model.TokopointsDrawerHomeData
import com.tokopedia.home.beranda.domain.gql.ProductrevDismissSuggestion
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.home.beranda.helper.Result
import dagger.Lazy
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

    override fun getHomeData() = homeCachedDataSource.getCachedHomeData()

    override fun updateHomeData(): Flow<Result<Any>> = flow{
        val response = homeRemoteDataSource.getHomeData()
        response.getError(HomeData::class.java)?.let {
            if (it.isNotEmpty()) {
                if (!TextUtils.isEmpty(it[0].message)){
                    emit(Result.error(Throwable(it[0].message)))
                }
            }
        }
        val homeData = response.getSuccessData<HomeData?>()
        if (homeData == null) {
            homeCachedDataSource.saveToDatabase(homeDefaultDataSource.getDefaultHomeData())
        } else {
            homeCachedDataSource.saveToDatabase(homeData)
        }
        emit(Result.success(null))
    }

    override fun sendGeolocationInfo(): Observable<Response<String>> = geolocationRemoteDataSource.get().sendGeolocationInfo()

}