package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon
import com.tokopedia.home.beranda.domain.model.HomeIconData
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetHomeIconUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<HomeIconData>
) : UseCase<HomeIconData>(){
    private val params = RequestParams.create()

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(HomeIconData::class.java)
    }

    override suspend fun executeOnBackground(): HomeIconData {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }
}