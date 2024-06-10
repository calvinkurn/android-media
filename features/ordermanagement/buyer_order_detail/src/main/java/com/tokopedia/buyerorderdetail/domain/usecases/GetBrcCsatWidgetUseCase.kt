package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buyerorderdetail.domain.models.GetBrcCsatWidgetRequestParams
import com.tokopedia.buyerorderdetail.domain.models.GetBrcCsatWidgetRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBrcCsatWidgetResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import org.json.JSONObject
import javax.inject.Inject

class GetBrcCsatWidgetUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val repository: GraphqlRepository
) : BaseGraphqlUseCase<GetBrcCsatWidgetRequestParams, GetBrcCsatWidgetRequestState>(dispatchers) {
    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: GetBrcCsatWidgetRequestParams): Flow<GetBrcCsatWidgetRequestState> =
        flow {
            emit(GetBrcCsatWidgetRequestState.Requesting)
            emit(GetBrcCsatWidgetRequestState.Complete.Success(sendRequest(params).resolutionGetCsatFormV4))
        }.catch {
            emit(GetBrcCsatWidgetRequestState.Complete.Error(it))
        }

    private fun createRequestParam(params: GetBrcCsatWidgetRequestParams): Map<String, Any> {
        return RequestParams.create().apply {
            putString(PARAM_INPUT, JSONObject().put(PARAM_ORDER_ID, params.orderId).toString())
        }.parameters
    }

    private suspend fun sendRequest(
        params: GetBrcCsatWidgetRequestParams
    ): GetBrcCsatWidgetResponse.Data {
        return repository.request(
            graphqlQuery(),
            createRequestParam(params),
            getCacheStrategy(params.shouldCheckCache)
        )
    }

    companion object {
        private const val PARAM_INPUT = "param"
        private const val PARAM_ORDER_ID = "orderID"
        private const val QUERY = """
            query BomGetBrcCsatWidget($$PARAM_INPUT: String) {
              resolution_get_csat_form_v4(param: $$PARAM_INPUT) {
                data {
                  isEligible
                }
              }
            }
        """
    }
}
