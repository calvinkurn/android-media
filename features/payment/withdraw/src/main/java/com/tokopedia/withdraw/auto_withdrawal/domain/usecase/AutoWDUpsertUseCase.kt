package com.tokopedia.withdraw.auto_withdrawal.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.withdraw.auto_withdrawal.QUERY_UPSERT_AUTO_WD_DATA
import com.tokopedia.withdraw.auto_withdrawal.domain.model.AutoWDStatus
import com.tokopedia.withdraw.auto_withdrawal.domain.model.AutoWDStatusResponse
import javax.inject.Inject
import javax.inject.Named

class AutoWDUpsertUseCase @Inject constructor(
        @Named(QUERY_UPSERT_AUTO_WD_DATA) val query: String, graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<AutoWDStatusResponse>(graphqlRepository) {

    fun getAutoWDUpsert(onSuccess: (AutoWDStatus) -> Unit,
                        onError: (Throwable) -> Unit) {
        try {
            this.setTypeClass(AutoWDStatusResponse::class.java)
            this.setGraphqlQuery(query)
            this.execute(
                    { result ->
                        onSuccess(result.autoWDStatus)
                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

}