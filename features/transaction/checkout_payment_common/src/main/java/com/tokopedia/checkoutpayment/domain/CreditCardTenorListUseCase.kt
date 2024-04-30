package com.tokopedia.checkoutpayment.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkoutpayment.data.CreditCardTenorList
import com.tokopedia.checkoutpayment.data.CreditCardTenorListRequest
import com.tokopedia.checkoutpayment.data.CreditCardTenorListResponse
import com.tokopedia.checkoutpayment.data.TenorListItem
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class CreditCardTenorListUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<CreditCardTenorListRequest, CreditCardTenorListData>(dispatchers.io) {

    private fun generateParam(input: CreditCardTenorListRequest): Map<String, Any?> {
        return mapOf(INPUT to input)
    }

    private fun mapCreditCardTenorListData(ccTenorListGqlResponse: CreditCardTenorList): CreditCardTenorListData {
        return CreditCardTenorListData(
            processTime = ccTenorListGqlResponse.processTime,
            errorCode = ccTenorListGqlResponse.errorCode,
            errorMsg = ccTenorListGqlResponse.errorMsg,
            tenorList = mapTenorList(ccTenorListGqlResponse.tenorList)
        )
    }

    private fun mapTenorList(listTenorGql: List<TenorListItem>): List<TenorListData> {
        val arrayListTenor = arrayListOf<TenorListData>()
        listTenorGql.forEach {
            val tenor = TenorListData(
                type = it.type,
                bank = it.bank,
                desc = it.description,
                amount = it.amount,
                fee = it.fee,
                rate = it.rate,
                disable = it.disabled,
                gatewayCode = it.gatewayCode
            )
            arrayListTenor.add(tenor)
        }
        return arrayListTenor
    }

    companion object {
        const val INPUT = "input"

        val QUERY = """
            mutation creditCardTenorList(${'$'}input: CreditCardTenorListRequest!) {
                creditCardTenorList(input:${'$'}input) {
                    process_time
                    error_code
                    error_msg
                    tenor_list {
                        type
                        bank
                        description
                        amount
                        fee
                        rate
                        disabled
                        gateway_code
                    }
                }
            }
        """.trimIndent()
    }

    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: CreditCardTenorListRequest): CreditCardTenorListData {
        val request =
            GraphqlRequest(QUERY, CreditCardTenorListResponse::class.java, generateParam(params))
        val response = graphqlRepository.response(listOf(request))
            .getSuccessData<CreditCardTenorListResponse>()
        return mapCreditCardTenorListData(response.ccTenorList)
    }
}
