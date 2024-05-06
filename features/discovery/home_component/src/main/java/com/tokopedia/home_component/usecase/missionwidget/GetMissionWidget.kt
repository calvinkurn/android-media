package com.tokopedia.home_component.usecase.missionwidget

import android.os.Bundle
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home_component.query.MissionWidgetQuery
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by dhaba
 */
class GetMissionWidget @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<HomeMissionWidgetData.HomeMissionWidget>
) : UseCase<HomeMissionWidgetData.HomeMissionWidget>() {

    init {
        graphqlUseCase.setTypeClass(HomeMissionWidgetData.HomeMissionWidget::class.java)
        graphqlUseCase.setCacheStrategy(
            GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        )
    }

    companion object {
        private const val TYPE = "type"
        private const val DEFAULT_TYPE = "home_mission"
        const val BANNER_LOCATION_PARAM = "location"
        const val PARAM = "param"
    }

    override suspend fun executeOnBackground(): HomeMissionWidgetData.HomeMissionWidget {
        return graphqlUseCase.executeOnBackground()
    }

    suspend fun execute(bundle: Bundle): HomeMissionWidgetData.HomeMissionWidget {
        val params = RequestParams.create()
        bundle.getString(BANNER_LOCATION_PARAM, "")?.let {
            params.putString(BANNER_LOCATION_PARAM, it)
        }
        bundle.getString(PARAM, "")?.let {
            params.putString(PARAM, it)
        }
        params.putString(TYPE, DEFAULT_TYPE)
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(MissionWidgetQuery())
        graphqlUseCase.setRequestParams(params.parameters)
        return executeOnBackground()
    }
}
