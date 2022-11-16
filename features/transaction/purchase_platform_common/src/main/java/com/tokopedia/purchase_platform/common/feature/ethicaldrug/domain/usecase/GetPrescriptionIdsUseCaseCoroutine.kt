package com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response.GetPrescriptionIdsResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetPrescriptionIdsUseCaseCoroutine @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository
) : UseCase<GetPrescriptionIdsResponse>() {

    private var params: Map<String, Any?>? = null

    fun setParams(checkoutId: String, source: String? = SOURCE_CHECKOUT): GetPrescriptionIdsUseCaseCoroutine {
        params = mapOf(
            PARAM_CHECKOUT_ID to checkoutId,
            PARAM_SOURCE to source
        )
        return this
    }

    @GqlQuery(QUERY_PRESCRIPTION_IDS, GET_PRESCRIPTION_IDS_QUERY)
    override suspend fun executeOnBackground(): GetPrescriptionIdsResponse {
        params?.let {
            val request = GraphqlRequest(
                PrescriptionIdsQuery(),
                GetPrescriptionIdsResponse::class.java,
                it
            )
            return graphqlRepository.response(listOf(request))
                .getSuccessData()
        } ?: throw RuntimeException("Param must be initialized")
    }

    companion object {
        private const val PARAM_CHECKOUT_ID = "checkout_id"
        private const val PARAM_SOURCE = "source"

        private const val QUERY_PRESCRIPTION_IDS = "PrescriptionIdsQuery"

        const val SOURCE_CHECKOUT = "checkout"
        const val SOURCE_OCC = "occ"
    }
}
