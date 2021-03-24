package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.model.TokopointsDrawerHomeData
import com.tokopedia.home.beranda.data.model.TokopointsDrawerListHomeData
import com.tokopedia.home.beranda.domain.gql.tokopoint.TokopointQuery
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetHomeTokopointsListDataUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<TokopointsDrawerListHomeData>
) : UseCase<TokopointsDrawerListHomeData>() {
    private val params = RequestParams.create()

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(TokopointsDrawerListHomeData::class.java)
    }

    override suspend fun executeOnBackground(): TokopointsDrawerListHomeData {
        graphqlUseCase.clearCache()
        setParams()
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    fun setParams() {
        params.parameters.clear()
        params.putString(API_VERSION, API_VERSION_VALUE)
    }

    companion object {
        private const val API_VERSION = "apiVersion"
        private const val API_VERSION_VALUE = "3.0.0"
    }
}