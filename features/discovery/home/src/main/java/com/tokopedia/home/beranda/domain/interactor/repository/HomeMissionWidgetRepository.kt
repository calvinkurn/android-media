package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.model.HomeMissionWidgetData
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by dhaba
 */
class HomeMissionWidgetRepository @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<HomeMissionWidgetData.HomeMissionWidget>
)
    : UseCase<HomeMissionWidgetData.HomeMissionWidget>(), HomeRepository<HomeMissionWidgetData.HomeMissionWidget> {
    init {
        graphqlUseCase.setTypeClass(HomeMissionWidgetData.HomeMissionWidget::class.java)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    private val params = RequestParams.create()

    companion object{
        private const val TYPE = "type"
        private const val DEFAULT_TYPE = "home_mission"
        const val BANNER_LOCATION_PARAM = "location"
    }

    override suspend fun executeOnBackground(): HomeMissionWidgetData.HomeMissionWidget {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    private fun generateParam(bundle: Bundle) {
        bundle.getString(BANNER_LOCATION_PARAM, "")?.let {
            params.putString(BANNER_LOCATION_PARAM, it)
        }
        params.putString(TYPE, DEFAULT_TYPE)
    }

    override suspend fun getRemoteData(bundle: Bundle): HomeMissionWidgetData.HomeMissionWidget {
        generateParam(bundle)
        return executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): HomeMissionWidgetData.HomeMissionWidget {
        return HomeMissionWidgetData.HomeMissionWidget()
    }
}