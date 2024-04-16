package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.model.HomeAtfData
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class HomeAtfRepository @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<HomeAtfData>
) : UseCase<HomeAtfData>(), HomeRepository<HomeAtfData> {

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(HomeAtfData::class.java)
    }

    override suspend fun executeOnBackground(): HomeAtfData {
        graphqlUseCase.clearCache()
        return graphqlUseCase.executeOnBackground()
    }

    override suspend fun getRemoteData(bundle: Bundle): HomeAtfData {
        return executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): HomeAtfData {
        return HomeAtfData()
    }
}
