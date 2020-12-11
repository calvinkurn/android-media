package com.tokopedia.paylater.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.paylater.GQL_PAYLATER_APPLICATION_STATUS
import com.tokopedia.paylater.domain.model.PayLaterApplicationStatusResponse
import com.tokopedia.paylater.domain.model.UserCreditApplicationStatus
import javax.inject.Inject
import javax.inject.Named

class PayLaterApplicationStatusUseCase @Inject constructor(
        @Named(GQL_PAYLATER_APPLICATION_STATUS) val query: String, graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<PayLaterApplicationStatusResponse>(graphqlRepository) {

    fun getPayLaterApplicationStaus(onSuccess: (UserCreditApplicationStatus) -> Unit,
                        onError: (Throwable) -> Unit) {
        try {
            this.setTypeClass(PayLaterApplicationStatusResponse::class.java)
            this.setGraphqlQuery(query)
            this.execute(
                    { result ->
                        onSuccess(result.userCreditApplicationStatus)

                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }
}