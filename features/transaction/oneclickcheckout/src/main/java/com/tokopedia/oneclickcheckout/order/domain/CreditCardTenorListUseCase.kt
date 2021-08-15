package com.tokopedia.oneclickcheckout.order.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.oneclickcheckout.order.data.creditcard.CreditCardTenorListRequest
import com.tokopedia.oneclickcheckout.order.data.creditcard.CreditCardTenorListResponse
import com.tokopedia.oneclickcheckout.order.data.creditcard.TenorListItem
import com.tokopedia.oneclickcheckout.order.view.model.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 05/08/21.
 */
class CreditCardTenorListUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository) {

    suspend fun executeSuspend(param: CreditCardTenorListRequest): CreditCardTenorListData {
        val request = GraphqlRequest(QUERY, CreditCardTenorListResponse::class.java, generateParam(param))
        val response = graphqlRepository.getReseponse(listOf(request)).getSuccessData<CreditCardTenorListResponse>()
        return mapCreditCardTenorListData(response)
    }

    private fun generateParam(input: CreditCardTenorListRequest): Map<String, Any?> {
        return mapOf(INPUT to input)
    }

    private fun mapCreditCardTenorListData(creditCardTenorListGqlResponse: CreditCardTenorListResponse): CreditCardTenorListData {
        return CreditCardTenorListData(
            processTime = creditCardTenorListGqlResponse.processTime,
            errorCode = creditCardTenorListGqlResponse.errorCode,
            errorMsg = creditCardTenorListGqlResponse.errorMsg,
            tenorList = mapTenorList(creditCardTenorListGqlResponse.tenorList)
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
                disable = it.disabled
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
                    }
                }
            }
        """.trimIndent()
    }
}