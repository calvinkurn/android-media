package com.tokopedia.minicart.bmgm.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.minicart.bmgm.domain.model.SetCartlistCheckboxGqlResponse
import com.tokopedia.purchase_platform.common.feature.bmgm.data.request.SetCartlistCheckboxStateRequest
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class SetCartListCheckboxStateUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository, dispatchers: CoroutineDispatchers
) : CoroutineUseCase<SetCartlistCheckboxGqlResponse>(graphqlRepository, dispatchers) {

    companion object {
        private const val PARAM = "params"
        private const val GQL_QUERY =
            "mutation setCheckboxState(${'$'}params: [CartCheckboxStateParam]) { set_cartlist_checkbox_state(params: ${'$'}params) { error_message data { success } } }"
    }

    override val classType: Class<SetCartlistCheckboxGqlResponse>
        get() = SetCartlistCheckboxGqlResponse::class.java

    override fun graphqlQuery(): String = GQL_QUERY

    suspend operator fun invoke(cartIds: List<String>): Boolean {
        try {
            val response = execute(createParam(cartIds))
            val data = response.setCartlistCheckboxStateResponse
            if (data.errorMessage.isEmpty()) {
                return data.data.success == Int.ONE
            } else {
                throw RuntimeException(data.errorMessage.joinToString { " - " })
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
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
