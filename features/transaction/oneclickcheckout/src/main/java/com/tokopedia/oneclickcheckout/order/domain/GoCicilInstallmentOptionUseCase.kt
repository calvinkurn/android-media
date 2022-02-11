package com.tokopedia.oneclickcheckout.order.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.oneclickcheckout.order.data.creditcard.CreditCardTenorList
import com.tokopedia.oneclickcheckout.order.data.creditcard.CreditCardTenorListRequest
import com.tokopedia.oneclickcheckout.order.data.creditcard.TenorListItem
import com.tokopedia.oneclickcheckout.order.view.model.CreditCardTenorListData
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentGoCicilTerms
import com.tokopedia.oneclickcheckout.order.view.model.TenorListData
import kotlinx.coroutines.delay
import javax.inject.Inject

class GoCicilInstallmentOptionUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository) {

    @GqlQuery(GoCicilInstallmentOptionQuery, QUERY)
    suspend fun executeSuspend(param: CreditCardTenorListRequest): List<OrderPaymentGoCicilTerms> {
//        val request = GraphqlRequest(GoCicilInstallmentOptionQuery(), CreditCardTenorListResponse::class.java, generateParam(param))
//        val response = graphqlRepository.response(listOf(request)).getSuccessData<CreditCardTenorListResponse>()
        delay(5_000)
        return listOf(
                OrderPaymentGoCicilTerms(isActive = true),
                OrderPaymentGoCicilTerms(isActive = false),
                OrderPaymentGoCicilTerms(isActive = true, installmentTerm = 1),
        )
    }

    fun generateParam(input: CreditCardTenorListRequest): Map<String, Any?> {
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
                    disable = it.disabled
            )
            arrayListTenor.add(tenor)
        }
        return arrayListTenor
    }

    companion object {
        const val INPUT = "input"

        const val GoCicilInstallmentOptionQuery = "GoCicilInstallmentOptionQuery"
        const val QUERY = """
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
        """
    }
}