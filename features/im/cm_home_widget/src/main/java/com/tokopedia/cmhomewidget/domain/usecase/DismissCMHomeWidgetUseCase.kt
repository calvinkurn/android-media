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
class DismissCMHomeWidgetUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<DeleteCMHomeWidgetDataGqlResponse>(graphqlRepository) {

    fun deleteCMHomeWidgetData(
        onSuccess: (DeleteCMHomeWidgetDataResponse) -> Unit,
        onError: (Throwable) -> Unit,
        parentId: Long,
        campaignId: Long
    ) {
        try {
            this.setTypeClass(DeleteCMHomeWidgetDataGqlResponse::class.java)
            this.setRequestParams(getRequestParams(parentId, campaignId))
            this.setGraphqlQuery(DismissCMHomeWidgetGQLQuery())
            this.execute(
                { result ->
                    onSuccess(result.deleteCMHomeWidgetDataResponse)
                }, { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(parentID: Long, campaignID: Long) = mutableMapOf(
        KEY_PARENT_ID to parentID.toInt(),
        KEY_CAMPAIGN_ID to campaignID.toInt()
    )

    companion object {
        const val KEY_PARENT_ID = "parentID"
        const val KEY_CAMPAIGN_ID = "campaignID"
    }
}