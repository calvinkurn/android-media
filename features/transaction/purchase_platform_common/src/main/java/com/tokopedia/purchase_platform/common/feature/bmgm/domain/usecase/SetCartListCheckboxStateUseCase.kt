package com.tokopedia.purchase_platform.common.feature.bmgm.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.purchase_platform.common.feature.bmgm.data.request.SetCartlistCheckboxStateRequest
import com.tokopedia.purchase_platform.common.feature.bmgm.data.response.SetCartlistCheckboxGqlResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("SetCartListCheckboxStateQuery", SetCartListCheckboxStateUseCase.SET_CHECKBOX_STATE_QUERY)
class SetCartListCheckboxStateUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<SetCartlistCheckboxGqlResponse>(graphqlRepository) {

    companion object {
        private const val PARAM = "params"

        const val SET_CHECKBOX_STATE_QUERY = """
            mutation setCheckboxState(${'$'}params: [CartCheckboxStateParam]) {
              set_cartlist_checkbox_state(params: ${'$'}params) {
                status
                error_message
                data {
                  success
                }
              }
            }
        """
    }

    init {
        setGraphqlQuery(SetCartListCheckboxStateQuery())
        setTypeClass(SetCartlistCheckboxGqlResponse::class.java)
    }

    suspend operator fun invoke(cartIds: List<String>): Boolean {
        try {
            setRequestParams(createParam(cartIds).parameters)
            val response = executeOnBackground()
            return response.setCartlistCheckboxStateResponse.data.success == Int.ONE
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }
    }

    private fun createParam(cartIds: List<String>): RequestParams {
        val cartListCheckboxStateRequestList = cartIds.map {
            SetCartlistCheckboxStateRequest(
                cartId = it, checkboxState = true
            )
        }
        return RequestParams.create().apply {
            putObject(PARAM, cartListCheckboxStateRequestList)
        }
    }
}
