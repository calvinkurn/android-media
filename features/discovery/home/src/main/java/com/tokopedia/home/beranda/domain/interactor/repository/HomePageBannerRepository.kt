package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.datasource.local.HomeRoomDataSource
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.home.beranda.domain.model.banner.HomeBannerData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class HomePageBannerRepository @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<HomeBannerData>,
        private val homeRoomDataSource: HomeRoomDataSource
) : UseCase<HomeBannerData>(), HomeRepository<HomeBannerData> {
    private val params = RequestParams.create()

    companion object {
        const val BANNER_LOCATION_PARAM = "location"
    }

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(HomeBannerData::class.java)
    }

    override suspend fun executeOnBackground(): HomeBannerData {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    override suspend fun getRemoteData(bundle: Bundle): HomeBannerData {
        bundle.getString(BANNER_LOCATION_PARAM, "")?.let {
            params.putString(BANNER_LOCATION_PARAM, it)
        }
        return executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): HomeBannerData {
        return HomeBannerData()
    }
}