package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.home.beranda.domain.model.HomeFlagData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class HomeFlagRepository @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<HomeFlagData>
) : UseCase<HomeFlagData>(), HomeRepository<HomeFlagData> {
    private var currentFlag: HomeFlagData? = null
    private val params = RequestParams.create()

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(HomeFlagData::class.java)
    }

    override suspend fun executeOnBackground(): HomeFlagData {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    override suspend fun getRemoteData(bundle: Bundle): HomeFlagData {
        this.currentFlag = executeOnBackground()
        return currentFlag!!
    }

    override suspend fun getCachedData(bundle: Bundle): HomeFlagData {
        currentFlag?.let { return it }
        return getRemoteData()
    }
}