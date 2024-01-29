package com.tokopedia.notifications.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.notifications.domain.data.AnimationScratchPopupResponse
import com.tokopedia.notifications.domain.data.GamiScratchCardPreEvaluate
import com.tokopedia.notifications.domain.query.GQL_QUERY_GAMI_ANIMATION_POPUP
import com.tokopedia.notifications.domain.query.GetAnimationPopupDataGQLQuery
import javax.inject.Inject
import kotlin.collections.HashMap


@GqlQuery("GetGamiScratchCardPreEvaluate", GQL_QUERY_GAMI_ANIMATION_POPUP)
class AnimationScratchPopupUseCase @Inject constructor(graphqlRepository: GraphqlRepository):
    GraphqlUseCase<AnimationScratchPopupResponse>(graphqlRepository) {

    fun getAnimationPopupData(
        onSuccess: (GamiScratchCardPreEvaluate) -> Unit,
        onError: (Throwable) -> Unit,
        pageSource: String
    ) {
        try {
            this.setTypeClass(AnimationScratchPopupResponse::class.java)
            this.setGraphqlQuery(GetAnimationPopupDataGQLQuery())
            this.setRequestParams(getRequestParams(pageSource))
            this.execute(
                { result ->
                    if (result.gamiScratchCardPreEvaluate.resultStatus?.code == "200") {
                        onSuccess(result.gamiScratchCardPreEvaluate)
                    } else {
                        onError(Throwable())
                    }
                }, { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(pageSource: String): HashMap<String, Any> {
        val requestParams = HashMap<String, Any>()
        requestParams[SLUG] = ""
        requestParams[SOURCE] = pageSource
        return requestParams
    }

    companion object {
        private const val SOURCE = "source"
        private const val SLUG = "slug"
    }
}
