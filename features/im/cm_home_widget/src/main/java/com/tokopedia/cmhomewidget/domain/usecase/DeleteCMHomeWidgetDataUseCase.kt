package com.tokopedia.cmhomewidget.domain.usecase


import com.tokopedia.cmhomewidget.domain.query.GQL_DELETE_CM_HOME_WIDGET_DATA
import com.tokopedia.cmhomewidget.domain.data.DeleteCMHomeWidgetDataGqlResponse
import com.tokopedia.cmhomewidget.domain.data.DeleteCMHomeWidgetDataResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery("DeleteCMHomeWidgetData", GQL_DELETE_CM_HOME_WIDGET_DATA)
class DeleteCMHomeWidgetDataUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<DeleteCMHomeWidgetDataGqlResponse>(graphqlRepository) {

    fun deleteCMHomeWidgetData(
        onSuccess: (DeleteCMHomeWidgetDataResponse) -> Unit,
        onError: (Throwable) -> Unit,
        parentID: Long,
        campaignID: Long
    ) {
        try {
            this.setTypeClass(DeleteCMHomeWidgetDataGqlResponse::class.java)
            this.setRequestParams(getRequestParams(parentID.toInt(), campaignID.toInt()))
            this.setGraphqlQuery(DeleteCMHomeWidgetData.GQL_QUERY)
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

    private fun getRequestParams(parentID: Int, campaignID: Int) = mutableMapOf(
        KEY_PARENT_ID to parentID,
        KEY_CAMPAIGN_ID to campaignID
    )

    companion object {
        const val KEY_PARENT_ID = "parentID"
        const val KEY_CAMPAIGN_ID = "campaignID"
    }
}