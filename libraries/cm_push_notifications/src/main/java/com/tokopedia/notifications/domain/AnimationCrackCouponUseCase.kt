package com.tokopedia.notifications.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.notifications.domain.data.AnimationCrackCouponResponse
import com.tokopedia.notifications.domain.data.GamiScratchCardCrack
import com.tokopedia.notifications.domain.query.GQL_QUERY_GAMI_ANIMATION_CRACK_COUPON
import com.tokopedia.notifications.domain.query.GetCrackCouponDataGQLQuery
import javax.inject.Inject


@GqlQuery("GetGamiScratchCardCrackData", GQL_QUERY_GAMI_ANIMATION_CRACK_COUPON)
class AnimationCrackCouponUseCase @Inject constructor(graphqlRepository: GraphqlRepository):
    GraphqlUseCase<AnimationCrackCouponResponse>(graphqlRepository) {

    fun getAnimationCrackCouponData(
        onSuccess: (GamiScratchCardCrack) -> Unit,
        onError: (Throwable) -> Unit,
        slug: String?
    ) {
        try {
            this.setTypeClass(AnimationCrackCouponResponse::class.java)
            this.setGraphqlQuery(GetCrackCouponDataGQLQuery())
            this.setRequestParams(getRequestParams(slug))
            this.execute(
                { result ->
                    if (result.gamiScratchCardCrack.resultStatus?.code == "200") {
                        onSuccess(result.gamiScratchCardCrack)
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

    private fun getRequestParams(slug: String?): HashMap<String, Any> {
        val requestParams = HashMap<String, Any>()
        requestParams[SLUG] = ""
        slug?.let {
            requestParams[SLUG] = it
        }
        requestParams[SOURCE] = "tokopedia-home-page"
        return requestParams
    }

    companion object {
        private const val SOURCE = "source"
        private const val SLUG = "slug"
    }
}
