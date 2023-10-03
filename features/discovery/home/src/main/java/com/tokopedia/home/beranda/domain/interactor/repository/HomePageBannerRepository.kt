package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.di.module.query.HomeBannerV2Query
import com.tokopedia.home.beranda.di.module.query.HomeSlidesQuery
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.home.beranda.domain.model.banner.HomeBannerData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class HomePageBannerRepository @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<HomeBannerData>,
    private val isUsingV2: Boolean
) : UseCase<HomeBannerData>(), HomeRepository<HomeBannerData> {
    private val params = RequestParams.create()

    companion object {
        const val PARAM = "param"
        const val PARAM_LOCATION = "location"
    }

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(HomeBannerData::class.java)
    }

    override suspend fun executeOnBackground(): HomeBannerData {
        graphqlUseCase.clearCache()
        val query = if (isUsingV2) { HomeBannerV2Query() } else { HomeSlidesQuery() }
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    override suspend fun getRemoteData(bundle: Bundle): HomeBannerData {
        bundle.getString(PARAM_LOCATION, "")?.let {
            params.putString(PARAM_LOCATION, it)
        }
        bundle.getString(PARAM, "")?.let {
            params.putString(PARAM, it)
        }
        return executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): HomeBannerData {
        return HomeBannerData()
    }
}
