package com.tokopedia.mvc.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.mvc.data.mapper.UpdateVoucherMapper
import com.tokopedia.mvc.data.response.UpdateVoucherResponse
import com.tokopedia.mvc.domain.entity.UpdateVoucherResult
import com.tokopedia.mvc.domain.entity.Voucher
import javax.inject.Inject

class ChangeVoucherPeriodUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository,
    private val mapper: UpdateVoucherMapper
) : GraphqlUseCase<UpdateVoucherResult>(gqlRepository) {

    companion object {
        private val query = object : GqlQueryInterface {
            private val OPERATION_NAME = "ChangeVoucherPromo"
            val MUTATION = "mutation ChangeVoucherPromo(\$update_param: mvUpdateData!) {\n" +
                "  merchantPromotionUpdateMV(merchantVoucherUpdateData: \$update_param) {\n" +
                "    status\n" +
                "    message\n" +
                "    process_time\n" +
                "    data{\n" +
                "      redirect_url\n" +
                "      voucher_id\n" +
                "      status\n" +
                "    }\n" +
                "  }\n" +
                "}"

            override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
            override fun getQuery(): String = MUTATION
            override fun getTopOperationName(): String = OPERATION_NAME
        }
        const val UPDATE_PARAM_KEY = "update_param"
    }

    suspend fun execute(voucher: Voucher, token: String, startDate: String, startHour: String, endDate: String, endHour: String): UpdateVoucherResult {
        val request = buildRequest(voucher, token, startDate, startHour, endDate, endHour)
        val response = gqlRepository.response(listOf(request))
        val data = response.getSuccessData<UpdateVoucherResponse>()
        return mapper.mapToDomain(data)
    }

    private fun buildRequest(
        voucher: Voucher,
        token: String,
        startDate: String,
        startHour: String,
        endDate: String,
        endHour: String
    ): GraphqlRequest {
        return GraphqlRequest(
            query,
            UpdateVoucherResult::class.java,
            getParams(
                voucher,
                token,
                startDate,
                startHour,
                endDate,
                endHour
            )
        )
    }
    private fun getParams(
        voucher: Voucher,
        token: String,
        startDate: String,
        startHour: String,
        endDate: String,
        endHour: String
    ): Map<String, Any> {
        return mapOf(
            UPDATE_PARAM_KEY to mapper.createRequestBody(
                voucher,
                token,
                startDate,
                startHour,
                endDate,
                endHour
            )
        )
    }
}
