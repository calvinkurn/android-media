package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.data.model.HomeMissionWidgetData
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
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

    override suspend fun executeOnBackground(): HomeMissionWidgetData.HomeMissionWidget {
        try {
            graphqlUseCase.clearCache()
            graphqlUseCase.setRequestParams(generateParam())
            val response = graphqlUseCase.executeOnBackground()
            return response
        } catch (e: Exception) {
            return getCachedData()
        }
    }

    companion object{
        private const val TYPE = "type"
        private const val LOCATION = "location"
        private const val DEFAULT_VALUE = ""
    }

    private fun generateParam(): Map<String, Any?> {
        return mapOf(TYPE to DEFAULT_VALUE, LOCATION to DEFAULT_VALUE)
    }

    override suspend fun getRemoteData(bundle: Bundle): HomeMissionWidgetData.HomeMissionWidget {
        return executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): HomeMissionWidgetData.HomeMissionWidget {
        return HomeMissionWidgetData.HomeMissionWidget()
    }
}