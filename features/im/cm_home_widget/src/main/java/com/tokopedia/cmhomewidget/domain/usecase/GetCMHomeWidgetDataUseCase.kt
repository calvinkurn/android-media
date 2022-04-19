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

    private var previousRefreshTimeMillis = 0L

    fun getCMHomeWidgetData(
        onSuccess: (CMHomeWidgetDataResponse) -> Unit,
        onError: (Throwable) -> Unit,
        isForceRefresh: Boolean
    ) {
        try {
            if (isForceRefresh || isRefreshNeeded()) {
                this.setTypeClass(GetCMHomeWidgetDataGqlResponse::class.java)
                this.setGraphqlQuery(GetCMHomeWidgetGQLQuery())
                this.execute(
                    { result ->
                        if (result.cmHomeWidgetDataResponse.status == STATUS_SUCCESS_CODE) {
                            updatePreviousRefreshTime()
                            onSuccess(result.cmHomeWidgetDataResponse)
                        } else {
                            onError(Throwable())
                        }
                    }, { error ->
                        onError(error)
                    }
                )
            }
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun isRefreshNeeded(): Boolean {
        return (System.currentTimeMillis() - previousRefreshTimeMillis) > REFRESH_INTERVAL_MILLIS
    }

    private fun updatePreviousRefreshTime() {
        previousRefreshTimeMillis = System.currentTimeMillis()
    }

    companion object {
        const val REFRESH_INTERVAL_MILLIS = 60000L //60 sec
        const val STATUS_SUCCESS_CODE = 200
    }
}