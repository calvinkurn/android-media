package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.domain.model.HomeIconData
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetHomeIconUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<HomeIconData>
) : UseCase<HomeIconData>(){

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(HomeIconData::class.java)
    }

    override suspend fun executeOnBackground(): HomeIconData {
        return graphqlUseCase.executeOnBackground()
    }
}