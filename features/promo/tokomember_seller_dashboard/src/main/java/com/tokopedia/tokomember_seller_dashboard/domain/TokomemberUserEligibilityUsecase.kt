package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.model.TmUserElligibilityResponseData
import javax.inject.Inject

class TokomemberUserEligibilityUsecase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<TmUserElligibilityResponseData>(graphqlRepository) {

    @GqlQuery("IsUserEligible", IS_USER_ELIGIBLE)
    fun checkUserEligibility(
        success: (TmUserElligibilityResponseData) -> Unit,
        onFail: (Throwable) -> Unit,
    ) {
        this.setTypeClass(TmUserElligibilityResponseData::class.java)
        this.setGraphqlQuery(IS_USER_ELIGIBLE)
//        this.setRequestParams(getRequestParams())
        this.execute({
            success(it)
        }, {
            onFail(it)
        })
    }

    private fun getRequestParams(): Map<String, Any> {
        return mapOf(ACCESS_ID to 1, RESOURCE_ID to 1, RESOURCE_TYPE to "shop")
    }

    companion object {
        const val ACCESS_ID = "accessID"
        const val RESOURCE_ID = "resourceID"
        const val RESOURCE_TYPE = "resourceType"

    }
}

const val IS_USER_ELIGIBLE = """
    {
  rbacAuthorizeAccess(accessID: 1, resourceID: 1, resourceType: "shop") {
    error
    is_authorized
  }
}
"""