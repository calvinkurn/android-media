package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.model.TokopointsDrawerHomeData
import com.tokopedia.home.beranda.domain.gql.tokopoint.TokopointQuery
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetHomeTokopointsDataUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<TokopointsDrawerHomeData>
) : UseCase<TokopointsDrawerHomeData>() {
    private val params = RequestParams.create()

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(TokopointsDrawerHomeData::class.java)
    }

    override suspend fun executeOnBackground(): TokopointsDrawerHomeData {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    fun setParams(apiVersion: String) {
        params.parameters.clear()
        params.putString(API_VERSION, apiVersion)
    }

    companion object {
        private const val API_VERSION = "apiVersion"
    }
}