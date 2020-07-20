package com.tokopedia.thankyou_native.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.thankyou_native.GQL_CHECK_WHITE_LIST
import com.tokopedia.thankyou_native.domain.model.CheckWhiteListStatusResponse
import javax.inject.Inject
import javax.inject.Named

class CheckWhiteListStatusUseCase @Inject constructor(
        @Named(GQL_CHECK_WHITE_LIST) val query: String, graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<CheckWhiteListStatusResponse>(graphqlRepository) {

    fun getThankPageData(onSuccess: (Boolean) -> Unit,
                         onError: (Throwable) -> Unit) {
        try {
            this.setTypeClass(CheckWhiteListStatusResponse::class.java)
            this.setRequestParams(getRequestParams())
            this.setGraphqlQuery(query)
            this.execute(
                    {
                        onSuccess(true)
                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    private fun getRequestParams(): Map<String, Any> {
        return mapOf(PARAM_UPDATED_STATUS to true,
                PARAM_AUTH_VALUE to 1)
    }

    companion object {
        const val PARAM_UPDATED_STATUS = "updateStatus"
        const val PARAM_AUTH_VALUE = "authValue"
    }
}