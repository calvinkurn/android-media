package com.tokopedia.withdraw.auto_withdrawal.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.withdraw.auto_withdrawal.QUERY_GET_AUTO_WD_STATUS
import com.tokopedia.withdraw.auto_withdrawal.domain.model.AutoWDStatusResponse
import com.tokopedia.withdraw.auto_withdrawal.domain.model.AutoWDStatusData
import javax.inject.Inject
import javax.inject.Named

class AutoWDStatusUseCase @Inject constructor(
        @Named(QUERY_GET_AUTO_WD_STATUS) val query: String, graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<AutoWDStatusResponse>(graphqlRepository) {

    fun getAutoWDStatus(onSuccess: (AutoWDStatusData) -> Unit,
                        onError: (Throwable) -> Unit) {
        try {
            this.setTypeClass(AutoWDStatusResponse::class.java)
            this.setGraphqlQuery(query)
            this.execute(
                    { result ->
                        onSuccess(result.autoWDStatusData)
                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

}