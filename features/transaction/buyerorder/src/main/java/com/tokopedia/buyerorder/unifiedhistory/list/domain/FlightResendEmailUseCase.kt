package com.tokopedia.buyerorder.unifiedhistory.list.domain

import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.FlightResendEmail
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.LsPrintData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 07/08/20.
 */
class FlightResendEmailUseCase @Inject constructor(private val useCase: GraphqlUseCase<FlightResendEmail.Data>) {

    suspend fun execute(query: String, invoiceId: String, email: String): Result<FlightResendEmail.Data> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(FlightResendEmail.Data::class.java)
        useCase.setRequestParams(generateParam(invoiceId, email))

        return try {
            val finishOrder = useCase.executeOnBackground()
            Success(finishOrder)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(invoiceId: String, email: String): Map<String, Any?> {
        return mapOf(UohConsts.FLIGHT_GQL_PARAM_INVOICE_ID to invoiceId,
                UohConsts.FLIGHT_GQL_PARAM_EMAIL_ID to email)
    }
}