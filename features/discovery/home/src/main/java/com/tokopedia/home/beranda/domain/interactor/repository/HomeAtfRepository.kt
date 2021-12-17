package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.datasource.local.HomeRoomDataSource
import com.tokopedia.home.beranda.data.model.AtfData
import com.tokopedia.home.beranda.data.model.HomeAtfData
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class HomeAtfRepository @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<HomeAtfData>,
        private val homeRoomDataSource: HomeRoomDataSource
) : UseCase<HomeAtfData>(), HomeRepository<HomeAtfData> {
    private val params = RequestParams.create()

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(HomeAtfData::class.java)
    }

    override suspend fun executeOnBackground(): HomeAtfData {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    override suspend fun getRemoteData(bundle: Bundle): HomeAtfData {
        return executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): HomeAtfData {
        return HomeAtfData()
    }
}