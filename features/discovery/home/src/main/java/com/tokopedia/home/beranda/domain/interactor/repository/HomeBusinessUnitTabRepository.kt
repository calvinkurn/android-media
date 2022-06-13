package com.tokopedia.home.beranda.domain.interactor.repository

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class HomeBusinessUnitTabRepository @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<HomeWidget.Data>
) : UseCase<HomeWidget>() {
    private val params = RequestParams.create()
    init {
        graphqlUseCase.setTypeClass(HomeWidget.Data::class.java)
        params.parameters.clear()
    }
    override suspend fun executeOnBackground(): HomeWidget {
        graphqlUseCase.clearCache()
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground().homeWidget
    }
}