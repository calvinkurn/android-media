package com.tokopedia.sellerorder.list.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.list.domain.mapper.WaitingPaymentMapper
import com.tokopedia.sellerorder.list.domain.model.SomListWaitingPaymentResponse
import com.tokopedia.sellerorder.list.presentation.models.WaitingPaymentCounter
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class SomListGetWaitingPaymentUseCase @Inject constructor(
        private val useCase: GraphqlUseCase<SomListWaitingPaymentResponse.Data>,
        private val mapper: WaitingPaymentMapper) : BaseGraphqlUseCase() {

    init {
        useCase.setGraphqlQuery(QUERY)
    }

    suspend fun execute(): Result<WaitingPaymentCounter> {
        useCase.setTypeClass(SomListWaitingPaymentResponse.Data::class.java)

        val waitingPaymentCounter = useCase.executeOnBackground().orderFilterSom.waitingPaymentCounter
        return Success(mapper.mapResponseToUiModel(waitingPaymentCounter))
    }

    companion object {
        private val QUERY = """
            query OrderFilterSom {
              orderFilterSom {
                waiting_payment_counter {
                  text
                  amount
                }
              }
            }
        """.trimIndent()
    }
}