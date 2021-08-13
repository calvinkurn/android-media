package com.tokopedia.payment.setting.authenticate.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.payment.setting.authenticate.model.CheckWhiteListResponse
import com.tokopedia.payment.setting.authenticate.model.CheckWhiteListStatus
import com.tokopedia.payment.setting.util.GQL_CHECK_UPDATE_WHITE_LIST
import javax.inject.Inject

@GqlQuery("CheckUpdateWhiteList", GQL_CHECK_UPDATE_WHITE_LIST)
class CheckUpdateWhiteListCreditCartUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<CheckWhiteListResponse>(graphqlRepository) {

    fun whiteListResponse(
            onSuccess: (CheckWhiteListStatus) -> Unit,
            onError: (Throwable) -> Unit,
            authValue: Int, status: Boolean,
            token: String?,
    ) {
        try {
            this.setTypeClass(CheckWhiteListResponse::class.java)
            this.setGraphqlQuery(CheckUpdateWhiteList.GQL_QUERY)
            this.setRequestParams(getRequestParams(authValue, status, token))
            this.execute(
                    { result ->
                        if (result.checkWhiteListStatus == null)
                            onError(NullPointerException("Null Response"))
                        else onSuccess(result.checkWhiteListStatus!!)
                    },
                    { error -> onError(error) }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(
            authValue: Int, status: Boolean,
            token: String?,
    ): Map<String, Any?> {
        return mapOf(
                UPDATE_STATUS to status,
                AUTH_VALUE to authValue,
                TOKEN to token)
    }

    companion object {
        const val UPDATE_STATUS = "updateStatus"
        const val AUTH_VALUE = "authValue"
        const val TOKEN = "token"
    }

}