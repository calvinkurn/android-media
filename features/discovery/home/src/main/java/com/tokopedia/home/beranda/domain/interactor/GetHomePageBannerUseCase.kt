package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel
import com.tokopedia.home.beranda.domain.model.banner.HomeBannerData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetHomePageBannerUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<HomeBannerData>
) : UseCase<HomeBannerData>(){
    private val params = RequestParams.create()

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(HomeBannerData::class.java)
    }

    override suspend fun executeOnBackground(): HomeBannerData {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }
}