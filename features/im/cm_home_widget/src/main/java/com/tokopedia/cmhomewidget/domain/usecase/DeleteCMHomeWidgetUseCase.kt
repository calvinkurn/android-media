package com.tokopedia.cmhomewidget.domain.usecase


import com.tokopedia.cmhomewidget.domain.data.DeleteCMHomeWidgetDataGqlResponse
import com.tokopedia.cmhomewidget.domain.data.DeleteCMHomeWidgetDataResponse
import com.tokopedia.cmhomewidget.domain.query.DismissCMHomeWidgetGQLQuery
import com.tokopedia.cmhomewidget.domain.query.GQL_QUERY_DISMISS_CM_HOME_WIDGET
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery("DeleteCMHomeWidgetData", GQL_QUERY_DISMISS_CM_HOME_WIDGET)
class DeleteCMHomeWidgetUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<DeleteCMHomeWidgetDataGqlResponse>(graphqlRepository) {

    private var isRunning = false

    fun deleteCMHomeWidgetData(
        onSuccess: (DeleteCMHomeWidgetDataResponse) -> Unit,
        onError: (Throwable) -> Unit,
        parentId: String,
        campaignId: String
    ) {
        if(isRunning)
            return
        try {
            isRunning = true
            this.setTypeClass(DeleteCMHomeWidgetDataGqlResponse::class.java)
            this.setRequestParams(getRequestParams(parentId, campaignId))
            this.setGraphqlQuery(DismissCMHomeWidgetGQLQuery())
            this.execute(
                { result ->
                    isRunning = false
                    onSuccess(result.deleteCMHomeWidgetDataResponse)
                }, { error ->
                    isRunning = false
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            isRunning = false
            onError(throwable)
        }
    }

    private fun getRequestParams(parentID: String, campaignID: String) = mutableMapOf(
        KEY_PARENT_ID to parentID.toLong(),
        KEY_CAMPAIGN_ID to campaignID.toLong()
    )

    companion object {
        const val KEY_PARENT_ID = "parentID"
        const val KEY_CAMPAIGN_ID = "campaignID"
    }
}