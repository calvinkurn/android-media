package com.tokopedia.cmhomewidget.domain.usecase

import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetDataResponse
import com.tokopedia.cmhomewidget.domain.data.GetCMHomeWidgetDataGqlResponse
import com.tokopedia.cmhomewidget.domain.query.GQL_QUERY_GET_CM_HOME_WIDGET_DATA
import com.tokopedia.cmhomewidget.domain.query.GetCMHomeWidgetGQLQuery
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery("GetCMHomeWidgetData", GQL_QUERY_GET_CM_HOME_WIDGET_DATA)
class GetCMHomeWidgetDataUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<GetCMHomeWidgetDataGqlResponse>(graphqlRepository) {

    private var previousHitTimeMillis = 0L

    fun getCMHomeWidgetData(
        onSuccess: (CMHomeWidgetDataResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            if (canHitApi()) {
                this.setTypeClass(GetCMHomeWidgetDataGqlResponse::class.java)
                this.setGraphqlQuery(GetCMHomeWidgetGQLQuery())
                this.execute(
                    { result ->
                        updatePreviousHitTime()
                        onSuccess(result.cmHomeWidgetDataResponse)
                    }, { error ->
                        onError(error)
                    }
                )
            }
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun canHitApi(): Boolean {
        return (System.currentTimeMillis() - previousHitTimeMillis) > API_HIT_TIME_INTERVAL_MILLIS
    }

    private fun updatePreviousHitTime() {
        previousHitTimeMillis = System.currentTimeMillis()
    }

    companion object {
        const val API_HIT_TIME_INTERVAL_MILLIS = 60000L //60 sec
    }
}